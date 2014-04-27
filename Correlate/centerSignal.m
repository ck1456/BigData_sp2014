function [ output ] = centerSignal( input )
%centerSignal Center the input vector around zero
%   Simple shift of the values so that the range is centered on 0

cols = size(input,2);
maxi = max(input,[],2);
mini = min(input,[],2);
output = input - repmat(((maxi - mini) / 2) + mini,1, cols);

end

