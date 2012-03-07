require "eventmachine"

class BrainConnection < EventMachine::Connection

  def initialize(*args)
    super
    @broker = args[0]
  end
   
  def receive_data(data)
    p data

    #@broker.apply_data(data)

    #send_data data
    #close_connection_after_writing 
  end

  def unbind
    p ' connection totally closed'
  end

end