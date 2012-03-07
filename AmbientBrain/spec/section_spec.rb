require "spec_helper"

describe Section  do
	
	it "adds new receivers" do
		section = Section.new(:bathroom)
		section.add_receiver(:security)
		section.receivers.size.should eq 1
	end

end