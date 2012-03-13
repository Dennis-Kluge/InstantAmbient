require "spec_helper"

describe Converter do

	it "converts hashs to json" do
		Converter.to_json({"channels"=>["ARD", "ZDF", "ARTE"]}).should eq '{"channels":["ARD","ZDF","ARTE"]}'
	end

	it "converts hashs to xml" do
		Converter.to_xml({"channels"=>["ARD", "ZDF", "ARTE"]}).should eq "<opt>\n  <channels>ARD</channels>\n  <channels>ZDF</channels>\n  <channels>ARTE</channels>\n</opt>\n" 
	end

	it "converts hashs  to txt" do
		Converter.to_txt({"channels"=>["ARD", "ZDF", "ARTE"]}).should eq "channels - ARD, ZDF, ARTE, "
	end

end

