require "AmbientActor/version"
require "eventmachine"
require "xmlsimple" 
require "singleton"
require "serialport"


module AmbientActor
  
  class SerialConnector
    include Singleton

    def initialize      
    end

    def connect(device)
      port_str = device
      baud_rate = 9600  
      data_bits = 8  
      stop_bits = 1  
      parity = SerialPort::NONE  

      @serial_port = SerialPort.new(port_str, baud_rate, data_bits, stop_bits, parity)  
      @serial_port.sync = true
    end

    def send(values)      
      values.each do |k, v|
        v.each do |value|      
          value.each do |va, atr|
            case va

            when "light"
              @serial_port.write("0")
              @serial_port.write(extract_value(atr))   
            
            when "red"
              @serial_port.write("1")
              @serial_port.write(extract_value(atr))          
            
            when "green"
              @serial_port.write("2")
              @serial_port.write(extract_value(atr))
            
            when "blue"
              @serial_port.write("3")
              @serial_port.write(extract_value(atr))
            
            end    
          end          
        end
      end
    end

    def extract_value(value)
      calculation = value.to_f * 10
      puts "CALC: #{calculation}"
      if calculation > 9
        "9"
      else
        calculation.to_s
      end
    end
    
    
  end


	class ActorConnection < EventMachine::Connection
   
  	def receive_data(data)
    	p data
      hashed_data = XmlSimple.xml_in(data)    
      p hashed_data
      SerialConnector.instance.send(hashed_data)
      
  	end

  	def unbind
    	p ' connection closed'
  	end
  end

  def self.start(options) 
    s = SerialConnector.instance
    s.connect(options[:device])
  	EventMachine.run {
 			EventMachine::start_server(options[:host], options[:port], ActorConnection)
  		puts "Starting actor at port #{options[:port]} with device #{options[:device]}..."
		}
  end


end

