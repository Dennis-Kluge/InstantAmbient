require "eventmachine"
require "json"

require "ambient_brain/version"
require "ambient_brain/broker"
require "ambient_brain/receiver"
require "ambient_brain/section"
require "ambient_brain/connection"

broker = Broker.new do 

	section :security do
		to :security_system, :filter => [:json, :serialized], :secure => true		
		to :bathroom, :filter => [:json, :serialized], :secure => true		
	end

	section :media do
		to :tv, :filter => [:json, :serialized], :secure => true		
		to :stereo, :filter => [:json, :serialized], :secure => true		
	end

end

puts "===== ROUTER ====="
puts broker

EventMachine.run {
  EventMachine.connect('127.0.0.1', 8081, BrainConnection, broker)
  puts "Starting the brain..."
}




