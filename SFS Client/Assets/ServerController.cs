using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Sfs2X.Core;
using Sfs2X.Entities.Variables;
using Sfs2X.Requests;
using Sfs2X.Util;
using Sfs2X;
using Sfs2X.Exceptions;
using Sfs2X.Entities.Data;
using Sfs2X.Entities;

public class ServerController : MonoBehaviour {

    public SmartFox sfs;
    public ConfigData cfg;

    public static ServerController instance;

    void Awake()
    {
        // bu kısmı anlamışsındır? evett hepsi Extension handler kısmıda dahil mi evet 
        // tamam ozaman buraya yazmamı istediğin birşey var mı

        instance = this;

        sfs = new SmartFox();
        cfg = new ConfigData();

        cfg.Host = "127.0.0.1";
        cfg.Port = 9933;
        cfg.Zone = "BasicExamples";

        sfs.AddEventListener(SFSEvent.CONNECTION, OnConnection);
        sfs.AddEventListener(SFSEvent.LOGIN, OnLoginSuccess);
        sfs.AddEventListener(SFSEvent.LOGIN_ERROR, OnLoginError);
        sfs.AddEventListener(SFSEvent.EXTENSION_RESPONSE, OnExtensionHandler);
        sfs.AddEventListener(SFSEvent.ROOM_JOIN, RoomJoinSuccess);
        sfs.AddEventListener(SFSEvent.ROOM_JOIN_ERROR, RoomJoinError);

        sfs.Connect();
    }

    void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		if(this.sfs != null)
        {
            sfs.ProcessEvents();
        }
	}

    #region Connection
    private void OnConnection(BaseEvent evnt)
    { 
        bool connectionSuccess = (bool)evnt.Params["success"];
        if (connectionSuccess)
        {
            Debug.Log("Connection Success");
            sfs.Send(new LoginRequest("gokhan", "", this.cfg.Zone, new SFSObject()));
            return;
        }

        Debug.Log("Connection Failed!");
    }
    #endregion

    #region Login Success && Error
    private void OnLoginSuccess(BaseEvent evnt)
    {
        User loggedInUser = (User)evnt.Params["user"];
        Debug.Log("Login Success- User Name : " + loggedInUser.Name + " : " + loggedInUser.Id + " : " + loggedInUser.IsItMe);


        ISFSObject newParams = new SFSObject();
        sfs.Send(new ExtensionRequest("FindRoom", newParams)); 
        
    }

    private void OnLoginError(BaseEvent evnt)
    {

    }
    #endregion

    #region RoomHandlers
    private void RoomJoinSuccess(BaseEvent evnt)
    {
        Debug.Log("Room Join Success");
    }

    private void RoomJoinError(BaseEvent evnt)
    {
        Debug.Log("Room Join Error");
    }
    #endregion

    private void OnExtensionHandler(BaseEvent evnt)
    {
        string cmd = (string)evnt.Params["cmd"];
        Debug.Log(cmd);
        if(cmd == "HelloWorldResponse")
        {
            SFSObject prm = (SFSObject)evnt.Params["params"];
            string value = prm.GetUtfString("res");

            Debug.Log("Data recieved!!  " + value);
        }
    }


    private void OnApplicationQuit()
    {
        sfs.Disconnect(); // Eğer bunu yapmazsan server session kapatmıyor. unity donup kalabilir. Çünkü serverla bağlantı kopmuyor.
    }

}
