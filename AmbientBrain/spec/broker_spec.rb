require "spec_helper"

describe Broker do

	before do
		@valid_configuration = '
			{
				"id" : 2
			}'

		@invalid_configuration = '
			{
				"id" : 2
				"temperature" : 21
			}
		'

		@broker = Broker.new do

			section :bathroom do
				to :media_control, :filter => [:json], :secure => true
			end

			section :security do
			end
		end
	end

	it "holds no sections" do
		broker = Broker.new
		broker.sections.size.should eq 0
	end

	it "holds several sections" do		
		@broker.sections.size.should eq 2
	end

	it "holds receivers" do		
		@broker.sections[0].receivers.size.should eq 1
	end

	it "delegates valid configurations to actors" do
		@broker.apply_data(@valid_configuration).should eq true
	end

	it "handles invalid configurations as false" do
		@broker.apply_data(@invalid_configuration).should eq false 
	end


end