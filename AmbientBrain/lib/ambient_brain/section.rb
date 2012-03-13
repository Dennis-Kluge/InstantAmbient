class Section

	attr_reader :receivers, :name

	def initialize(name)
		@name = name
		@receivers = []
	end
	
	def add_receiver(receiver)		
		@receivers << receiver
	end

	def to_s
		puts "Section #{@namye}"
		@receivers.each do |r|
			puts r
		end
		puts "\n"
	end
end