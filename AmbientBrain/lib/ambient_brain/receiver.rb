class Receiver

	attr_reader :filters, :secure

	def initialize(receiver, options = {})		
		@receiver = receiver
		@filters = []
		@secure = false

		# write args
		options.each do |key, value|			
			case key
			when :filter
				@filters = value
			when :secure				
				@secure = value
			end						
		end		
	end	

	def to_s
		"	-> to #{@receiver} with filters #{@filters} and secure = #{@secure}"		
	end	
end