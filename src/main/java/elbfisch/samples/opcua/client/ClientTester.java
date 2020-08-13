/**
 * PROJECT   : Elbfisch - java process automation controller (jPac)
 * MODULE    : ClientTester.java
 * VERSION   : -
 * DATE      : -
 * PURPOSE   : 
 * AUTHOR    : Bernd Schuster, MSK Gesellschaft fuer Automatisierung mbH, Schenefeld
 * REMARKS   : -
 * CHANGES   : CH#n <Kuerzel> <datum> <Beschreibung>
 *
 * This file is part of the jPac process automation controller.
 * jPac is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jPac is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the jPac If not, see <http://www.gnu.org/licenses/>.
 */

package elbfisch.samples.opcua.client;

import static javafx.application.Application.launch;
import javafx.stage.Stage;
import org.jpac.Module;
import org.jpac.fx.DashboardLauncher;
import org.jpac.fx.test.ModuleTesterTemplate;

/**
 *
 * @author berndschuster
 */
public class ClientTester extends ModuleTesterTemplate{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Module module = new ClientMain();
        module.start();
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        try{
            DashboardLauncher dashboardLauncher = new DashboardLauncher();
            dashboardLauncher.show();
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
    }
}

