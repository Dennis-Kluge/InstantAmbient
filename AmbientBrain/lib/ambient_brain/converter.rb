require "xmlsimple"
require "json"

class Converter
	
	def self.to_txt(data)
		if data.class == Hash
			txt = ""
			data.each do |key, value|
				txt += "#{key} - "
				if value.class == Array 
					value.each do |v|						
						txt += "#{v}, "
					end					
				else
					txt += "#{value}"
				end
			end
			return txt
		end		
	end

	def self.to_json(data)		
		JSON.generate(data)		
	end

	def self.to_xml(data)		
		XmlSimple.xml_out(data)
	end
	
	
end