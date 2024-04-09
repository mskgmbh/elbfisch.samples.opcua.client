/**
 * PROJECT   : Elbfisch - java process automation controller (jPac)
 * MODULE    : ClientMain.java
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

import java.net.URI;
import org.jpac.CharString;
import org.jpac.Decimal;
import org.jpac.EventTimedoutException;
import org.jpac.InputInterlockException;
import org.jpac.Logical;
import org.jpac.Module;
import org.jpac.OutputInterlockException;
import org.jpac.PeriodOfTime;
import org.jpac.ProcessException;
import org.jpac.ShutdownRequestException;
import org.jpac.SignalInvalidException;
import org.jpac.SignedInteger;
import org.jpac.IoDirection;
import org.jpac.vioss.IoCharString;
import org.jpac.vioss.IoDecimal;
import org.jpac.vioss.IoLogical;
import org.jpac.vioss.IoSignedInteger;
import org.jpac.vioss.opcua.Handshake;

public class ClientMain extends Module{
    Logical       toggle;
    SignedInteger lastCommand;
    Decimal       analogValue;
    CharString    comment;    
    Handshake     handshake; 
 
    public ClientMain(){
        super(null, "Main");
        try{
            toggle      = new IoLogical(this, "toggle", new URI("opc.tcp://192.168.0.52:12686/elbfisch/2/Main.toggle"), IoDirection.INPUT);
            lastCommand = new IoSignedInteger(this, "lastCommand", new URI("opc.tcp://192.168.0.52:12686/elbfisch/2/Main.lastCommand"), IoDirection.INPUT);
            handshake   = new Handshake(this, "handshake", new URI("opc.tcp://192.168.0.52:12686/elbfisch/2/Main.handshake"));
            comment     = new IoCharString(this, "comment", new URI("opc.tcp://192.168.0.52:12686/elbfisch/2/Main.comment"), IoDirection.INPUT);
            analogValue = new IoDecimal(this, "analogValue", new URI("opc.tcp://192.168.0.52:12686/elbfisch/2/Main.analogValue"), IoDirection.INPUT);
        }
        catch(Exception exc){
            Log.error("Error:", exc);
        };
    }

    @Override
    protected void work() throws ProcessException {
        int command = 100;
        PeriodOfTime delay = new PeriodOfTime(1 * sec);
        try{
            Log.info("started: actual JVM: " + System.getProperty("java.version"));
            while(true){
                try{
                    if (!handshake.isValid() && !handshake.isReady()){
                        //wait, until server is available and ready to accept a command
                        Log.info("awaiting handshake getting valid ...");
                        handshake.valid().await();
                        Log.info("handshake got valid");
                    }
                    command++;
                    if (!handshake.isReady()) {
                    	handshake.ready().await();
                    }
                    delay.await();
                    handshake.request(command);
                    Log.info("request {} send to server", command);
                    handshake.active().await();
                    handshake.acknowledged().await(10 * sec);
                    Log.info("server acknowledged request {} with result {}", command, handshake.getResult());
                    handshake.resetRequest();
                    delay.await();
                }
                catch(ShutdownRequestException exc){
                    throw exc;
                }
                catch(EventTimedoutException | SignalInvalidException exc){
                    Log.error("handshake failed. Restarting protocol ...");
                    handshake.resetRequest();
                }
                catch(Exception exc){
                    Log.error("Error", exc);
                    handshake.resetRequest();
                }
            }
        }
        finally{
            Log.info("finished");
        }
    }

    @Override
    protected void preCheckInterlocks() throws InputInterlockException {
    }

    @Override
    protected void postCheckInterlocks() throws OutputInterlockException {
    }

    @Override
    protected void inEveryCycleDo() throws ProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
