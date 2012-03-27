require "eventmachine"
require "json"

require "ambient_brain/version"
require "ambient_brain/broker"
require "ambient_brain/receiver"
require "ambient_brain/section"
require "ambient_brain/connection"

broker = Broker.new do 

	receiver :security_system, :address => "127.0.0.1", :port => 9000, :filter => [:json, :serialized], :secure => true		
	receiver :actor, :address => "127.0.0.1", :port => 9123, :format => :txt, :secure => true		

	section :light do
		to :actor
	end


	section :media do
		to :tv
		to :stereo
	end

end

puts "===== ROUTER ====="
puts broker

EventMachine.run {
  # EventMachine.connect('127.0.0.1', 8081, BrainConnection, broker)
  EventMachine.start_server('127.0.0.1', 8081, BrainConnection, broker)
  puts "Starting the brain..."
}




