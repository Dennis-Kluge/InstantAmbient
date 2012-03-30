require "eventmachine"

class BrainConnection < EventMachine::Connection

  def initialize(*args)
    super
    @broker = args[0]
  end
   
  def receive_data(data)
    p data

    puts "RECEIVED: #{data}"
    if @broker.apply_data(data)
      send_data "success"  
    else
      send_data "error please try again"  
    end
    close_connection_after_writing     
  end

  def unbind
    p ' connection totally closed'
  end

end