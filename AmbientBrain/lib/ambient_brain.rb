require "eventmachine"
require "json"

require "ambient_brain/version"
require "ambient_brain/broker"
require "ambient_brain/receiver"
require "ambient_brain/section"
require "ambient_brain/connection"

module AmbientBrain

	@broker = Broker.new do 

		receiver :living_actor, :address => "127.0.0.1", :port => 9123, :format => :xml, :secure => true
		receiver :bedroom_actor, :address => "127.0.0.1", :port => 9124, :format => :xml, :secure => true		

		section :living_room do
			to :living_actor
		end

		section :bedroom do
			to :bedroom_actor
		end

	end

	def self.start(options)
		puts "===== ROUTER ====="
		puts @broker

		EventMachine.run {
		  EventMachine.start_server('127.0.0.1', 8081, BrainConnection, @broker)
		  puts "Starting the brain..."
		}
	end
end



