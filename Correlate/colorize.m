function [ color ] = colorize( val )
%colorize Create a web color string pased on the parameter value
%   Detailed explanation goes here

if val > 0
    color = sprintf('#00%02X00', uint8(val * 255));
    
else
    color = sprintf('#0000%02X', uint8(val * -255));
    
end

end

