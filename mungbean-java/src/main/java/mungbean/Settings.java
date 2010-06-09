/*
   Copyright 2009-2010 Janne Hietamäki

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

package mungbean;

public class Settings {
    private int maxOpenConnections = 10;
    private int initialConnections = 3;
    private boolean validateConnections = false;

    public int numberOfInitialConnections() {
        return initialConnections;
    }

    public void setNumberOfInitialConnections(int value) {
        this.initialConnections = value;
    }

    public int maximumNumberOfConcurrentConnections() {
        return maxOpenConnections;
    }

    public void setMaximumNumberOfConcurrentConnections(int value) {
        this.maxOpenConnections = value;
    }

    public boolean validateConnections() {
        return validateConnections;
    }

    public void setValidateConnections(boolean value) {
        this.validateConnections = value;
    }
}
