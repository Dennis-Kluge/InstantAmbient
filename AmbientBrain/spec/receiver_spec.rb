require "spec_helper"

describe Receiver do


	before do 
		@receiver = Receiver.new(:security, :secure => true, :format => :json)
		@receiver_xml = Receiver.new(:security, :secure => true, :format => :xml)
	end

	it "sets the right options" do
		@receiver = Receiver.new(:security, :secure => true, :format => :json)
		@receiver.secure.should eq true
		@receiver.format.should eq :json
	end


end