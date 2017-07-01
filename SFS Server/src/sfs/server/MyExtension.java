/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfs.server;

import com.smartfoxserver.v2.extensions.SFSExtension;

/**
 *
 * @author Whoaa
 */
public class MyExtension extends SFSExtension{
    
    @Override
    public void init(){ // Bu smartfoxun tanımladığı init methodu. Bu yüzden override ediyoruz.
        trace("Zone started");
        this.addRequestHandler(CMD.FindRoom, FindRoomHandler.class); // Client tarafından gelen HelloWorld adlı requestleri handle ediyor.
        // Gelen request datasını ikinci paremetrede vermiş olduğumuz HelloWorld.class a yönlendiriyor.
    }
}
