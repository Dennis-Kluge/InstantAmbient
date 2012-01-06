require "AmbientConnector/version"
require "java"
require "../jar/bluecove-2.1.1.jar"

import "javax.bluetooth.LocalDevice"
import "javax.bluetooth.DiscoveryAgent"

module AmbientConnector
  
  class Server
	  
	  def self.start
			serverUUID = "AmbientConnector";
      connectionString = "btgoep://localhost:" + serverUUID+ ";name=obex";
 
      
      LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent::GIAC);
      #      SessionNotifier notifier = (SessionNotifier)Connector.open(connectionString);
      #      notifier.acceptAndOpen(new RequestHandler());
		end
	end
	
	class RequestHandler
	end
	
end

AmbientConnector::Server.start
