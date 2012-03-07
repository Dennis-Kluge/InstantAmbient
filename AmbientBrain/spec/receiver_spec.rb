require "spec_helper"

describe Receiver do

	it "sets the right default values" do
		receiver = Receiver.new(:security)
		receiver.secure.should eq false
		receiver.filters.size.should eq 0
	end

	it "sets the right options" do
		receiver = Receiver.new(:security, :secure => true, :filter => [:json, :xml])
		receiver.secure.should eq true
		receiver.filters.size.should eq 2
	end

end