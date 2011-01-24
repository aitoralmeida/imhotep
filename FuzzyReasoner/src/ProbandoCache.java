import piramide.interaction.reasoner.CacheFactory;


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

public class ProbandoCache {
	
	public static void main(String [] args) throws Exception {
		
		ICalculadora original = new ICalculadora() {
			@Override
			public Integer suma(Integer a, Integer b) {
				System.out.println("sumando... " + a + "; " + b);
				return Integer.valueOf(a.intValue() + b.intValue());
			}
			
			@Override
			public Integer resta(Integer a, Integer b) {
				System.out.println("restando... " + a + "; " + b);
				return Integer.valueOf(a.intValue() - b.intValue());
			}
		};
		
		final CacheFactory<ICalculadora> cacheFactory = new CacheFactory<ICalculadora>(ICalculadora.class);
		final ICalculadora cal = cacheFactory.create(original, 1000);
		System.out.println(cal.suma(Integer.valueOf(5), Integer.valueOf(6)));
		System.out.println(cal.suma(Integer.valueOf(5), Integer.valueOf(6)));
		System.out.println(cal.suma(Integer.valueOf(5), Integer.valueOf(7)));
		System.out.println(cal.suma(Integer.valueOf(5), Integer.valueOf(7)));
		Thread.sleep(500);
		System.out.println(cal.resta(Integer.valueOf(5), Integer.valueOf(6)));
		Thread.sleep(600);
		System.out.println(cal.resta(Integer.valueOf(5), Integer.valueOf(6)));
		System.out.println(cal.suma(Integer.valueOf(5), Integer.valueOf(6)));
		System.out.println(cal.suma(Integer.valueOf(5), Integer.valueOf(7)));
		
		CacheFactory.shutdown();
	}
}
