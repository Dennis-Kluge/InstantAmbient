require "AmbientActor/version"
require "eventmachine"
require "xmlsimple" 


module AmbientActor
	
	class ActorConnection < EventMachine::Connection
   
  	def receive_data(data)
    	p data
      hashed_data = XmlSimple.xml_in(data)    
      p hashed_data
      
  	end

  	def unbind
    	p ' connection closed'
  	end
  end

  def self.start(options) 
  	EventMachine.run {
 			EventMachine::start_server(options[:host], options[:port], ActorConnection)
  		puts "Starting actor at port #{options[:port]}..."
		}
  end


end

