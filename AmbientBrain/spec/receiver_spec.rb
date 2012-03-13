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

	it "converts data to json" do
		@receiver.convert({"channels"=>["ARD", "ZDF", "ARTE"]}).should eq '{"channels":["ARD","ZDF","ARTE"]}'
	end

	it "converts data to xml" do
		@receiver_xml.convert({"channels"=>["ARD", "ZDF", "ARTE"]}).should eq "<opt>\n  <channels>ARD</channels>\n  <channels>ZDF</channels>\n  <channels>ARTE</channels>\n</opt>\n" 
	end

end