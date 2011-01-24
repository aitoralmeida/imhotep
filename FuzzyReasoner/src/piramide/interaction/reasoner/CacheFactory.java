package piramide.interaction.reasoner;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * Copyright (C) 2010 PIRAmIDE-SP3 authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Aitor Almeida <aitor.almeida@deusto.es>
 *         Pablo Orduña <pablo.orduna@deusto.es>
 *         Eduardo Castillejo <eduardo.castillejo@deusto.es>
 *
 */

public class CacheFactory<T>{
	
	private final Class<? extends T> klass;
	private static final ExecutorService executor = Executors.newCachedThreadPool();

	public CacheFactory(Class<? extends T> klass){
		this.klass = klass;
	}

	public static void shutdown(){
		CacheFactory.executor.shutdown();
	}

	@SuppressWarnings("unchecked")
	public T create(T original, long timeout){
		final CacheFactory.ProxyHandler<T> handler = new CacheFactory.ProxyHandler<T>(original, timeout);
		final Object result = Proxy.newProxyInstance(this.klass.getClassLoader(), new Class[]{ this.klass }, handler);
		return (T)result;
	}

	private static class ProxyHandler<T> implements InvocationHandler{
		
		private final T instance;
		private final long timeout;
		private final Map<String, Register> registry = new HashMap<String, Register>();
		private final Map<String, Future<Object>> tasks = new HashMap<String, Future<Object>>();
		
		private class Register{
			private final Object result;
			private final long lastTime;
			
			Register(Object result){
				this.lastTime = System.currentTimeMillis();
				this.result   = result;
			}
			
			boolean hasExpired(){
				final long current = System.currentTimeMillis();
				return current - this.lastTime > ProxyHandler.this.timeout;
			}
		}
		
		public ProxyHandler(T instance, long timeout){
			this.instance = instance;
			this.timeout = timeout;
		}
		
		private class Invocator implements Callable<Object>{

			private final Method method;
			private final Object [] args;
			
			Invocator(Method method, Object [] args){
				this.method = method;
				this.args = args;
			}
			
			@Override
			public Object call() throws Exception {
				final Method actualMethod = ProxyHandler.this.instance.getClass().getMethod(this.method.getName(), this.method.getParameterTypes());
				return actualMethod.invoke(ProxyHandler.this.instance, this.args);
			}
			
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			final StringBuilder builder = new StringBuilder();
			builder.append(method.toGenericString());
			if(args != null)
				for(Object arg : args){
					builder.append(";");
					builder.append(arg.hashCode());
				}
			
			final String identifier = builder.toString();
			final Future<Object> task;
			
			synchronized(this.registry){
				if(this.registry.containsKey(identifier)){
					final Register register = this.registry.get(identifier);
					if(!register.hasExpired())
						return register.result;
				}
				
				if(this.tasks.containsKey(identifier)){
					task = this.tasks.get(identifier);
				}else{
					final Invocator invocator = new Invocator(method, args);
					task = CacheFactory.executor.submit(invocator);
					this.tasks.put(identifier, task);
				}
			}
			
			final Object result = task.get();
			
			synchronized (this.registry) {
				if(!this.registry.containsKey(identifier) || this.registry.get(identifier).hasExpired()){
					this.registry.put(identifier, new Register(result));
					this.tasks.remove(identifier);
				}
			}
			
			return result;
		}
		
	}
}