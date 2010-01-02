/*
   Copyright 2009 Janne Hietamäki

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package mungbean.pojo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class ReflectionUtil {
    private ReflectionUtil() {
    }

    public static FieldDefinition[] fieldsOf(Class<?> typeClass) {
        ArrayList<FieldDefinition> ret = new ArrayList<FieldDefinition>();
        while (typeClass != null) {
            Field[] fields = typeClass.getDeclaredFields();
            for (Field field : fields) {
                if (isAccessible(field)) {
                    ret.add(new FieldDefinition(field));
                }
            }
            typeClass = typeClass.getSuperclass();
        }
        return ret.toArray(new FieldDefinition[ret.size()]);
    }

    private static boolean isAccessible(Field field) {
        return !field.isSynthetic() && !Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers());
    }
}
