require "spec_helper"
require "JSON"

describe Broker do

	before do
		@valid_configuration = '
			{
				"id" : 2, 
				"temperature" : 21, 
				"tv" : {
					"channels" : ["ARD", "ZDF", "ARTE"]
				},
				"bathroom" : {
					"temperature" : 22
				}
			}'

		@invalid_configuration = '
			{
				"id" : 2
				"temperature" : 21
			}
		'

		@broker = Broker.new do

			receiver :media_control, :ip => "127.0.0.1", :port => 5000, :format => :json, :secure => true
			receiver :main_control, :ip => "127.0.0.1", :port => 5001, :format => :xml, :secure => true

			section :bathroom do
				to :media_control
				to :main_control
			end

			section :tv do
				to :media_control
			end

			section :temperature do
				to :main_control
			end 

			section :security do
			end
		end
	end

	it "holds no sections" do
		broker = Broker.new
		broker.sections.length.should eq 0
	end

	it "holds several sections" do		
		@broker.sections.length.should eq 4
	end

	it "adds several receivers to a section" do		
		@broker.sections[:bathroom].length.should eq 2
	end

	it "holds receivers" do		
		@broker.receivers.size.should eq 2
	end

	it "delegates valid configurations to actors" do
			@broker.apply_data(@valid_configuration).should eq true
	end

	it "handles invalid configurations as false" do
		@broker.apply_data(@invalid_configuration).should eq false 
	end

	it "procceses a valid configuration" do	
		configuration = JSON.parse(@valid_configuration)			
		@broker.process_configuration(configuration).should_not eq nil
	end

	it "finds a receiver" do
	 	@broker.find_receiver(:main_control).name.should eq :main_control
	end

end