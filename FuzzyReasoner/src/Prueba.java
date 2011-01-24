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
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.sourceforge.jFuzzyLogic.FIS;

/**
 * Test parsing an FCL file
 * @author pcingola@users.sourceforge.net
 */
public class Prueba {
    public static void main(String[] args) throws Exception {
        // Load from 'FCL' file
        String fileName = "fcl/tipper.fcl";
        
        PrintStream syserr = System.err;
        
        Class<?> klass = Class.forName(System.class.getName());
        Field field = klass.getDeclaredField("err");
        field.setAccessible(true);
        
        final Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        
        field.set(null, ps);
        
        FIS fis = FIS.load(fileName,true);
        
        ps.flush();
        baos.flush();
        
        String fucking = baos.toString();
        System.out.println("--->" + fucking + "<---");
        
        field.set(null, syserr);
        
        System.err.println("joeeee");
        
        // Error while loading?
        if( fis == null ) { 
            System.err.println("Can't load file: '" 
                                   + fileName + "'");
            return;
        }

        // Show 
       // fis.chart();

        // Set inputs
        fis.setVariable("service", 8);
        fis.setVariable("food", 9);

        // Evaluate
        fis.evaluate();
        
        System.out.println("EL TEMA: " + fis.getVariable("service").getMembership("good"));
        
        System.out.println(fis.getVariable("tip").getUniverseMax());
        

        // Show output variable's chart 
        fis.getVariable("tip").chartDefuzzifier(true);

        // Print ruleSet
        // System.out.println(fis.getVariable("tip"));
        System.out.println(fis.getVariable("tip").defuzzify());
        System.out.println(fis.getVariable("tip").getUniverseMin());
        
        fis.chart();
    }
}
