require "spec_helper"
require "eventmachine"

describe BrainConnection do


	before do
		@json = <<HERE
  		{
  			"id" : 2
  		}
HERE

	end

	it "receives data" do

		# broker = Broker.new do 

		# 	section :security do
		# 		to :security_system, :filter => [:json, :serialized], :secure => true		
		# 		to :bathroom, :filter => [:json, :serialized], :secure => true		
		# 	end

		# 	section :media do
		# 		to :tv, :filter => [:json, :serialized], :secure => true		
		# 		to :stereo, :filter => [:json, :serialized], :secure => true		
		# 	end
		# end
		# connection = BrainConnection.new(broker)
		# connection.receive_data(@json).should eq true
	end
	
end