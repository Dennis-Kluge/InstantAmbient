require "AmbientActor/version"
require "eventmachine"


module AmbientActor
	
	class ActorConnection < EventMachine::Connection
   
  	def receive_data(data)
    	p data    
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

