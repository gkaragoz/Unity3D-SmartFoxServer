/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfs.server;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.match.BoolMatch;
import com.smartfoxserver.v2.entities.match.MatchExpression;
import com.smartfoxserver.v2.entities.match.RoomProperties;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.game.CreateSFSGameSettings;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Whoaa
 */
public class FindRoomHandler extends BaseClientRequestHandler{
    
    @Override
    public void handleClientRequest(User player, ISFSObject params){ //evet
        trace("Finding Room....");
        
        try {
            if(!FindRoom(player)){
                // Create new Room
                CreateNewGame(player);
                trace("Room Created!");
            } 
        } catch (SFSJoinRoomException ex) {
            Logger.getLogger(FindRoomHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SFSCreateRoomException ex) {
            Logger.getLogger(FindRoomHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean FindRoom(User player) throws SFSJoinRoomException{
        Zone zone = getParentExtension().getParentZone();
        
        MatchExpression exp = new MatchExpression(RoomProperties.HAS_FREE_PLAYER_SLOTS, BoolMatch.EQUALS, true);
        List<Room> joinableRooms = getApi().findRooms(zone.getRoomListFromGroup("GameRoom"), exp, 1);
        
        if(joinableRooms.size() > 0){
            getApi().joinRoom(player, joinableRooms.get(0));
            
            trace("Found Room!");
            return true;
        }
        
        return false;
    }
    
    private void CreateNewGame(User player) throws SFSCreateRoomException, SFSJoinRoomException{
        Zone zone = getParentExtension().getParentZone();
        // Onu sonra sen netten bakarsın.
        // Şu anlık room variable a da gerek yok.
        
        CreateSFSGameSettings roomSettings = new CreateSFSGameSettings();
        roomSettings.setName("GameRoom" + player.getId());
        roomSettings.setGame(false);
        roomSettings.setMaxUsers(20);
        roomSettings.setGroupId("GameRoom");
        //Burda bir sürü özellik var sonra bakarsın. odanın tag'ı gibi düşün. MatchMaking room or GameROom or Lobby
        // bi sigara içip gelcem. oky bok iç kafa kalmadı. bekliyom
        
        Room matchRoom = getApi().createRoom(zone, roomSettings, player);
        getApi().joinRoom(player, matchRoom);
    }
}
