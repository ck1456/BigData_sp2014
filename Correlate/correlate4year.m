
% Use cross correlation to see record the change over time of relevant
% health and job metrics
%
% author: chris keitel (ck1456@nyu.edu)

data = csvread('combined-results.csv', 1, 1);

% Pull out the AverageSeverity and the Unemployment percent
unemp = data(:, [4, 8, 12, 16]);
stay = data(:, [18, 29, 40, 51]);

% Need to center this data around 0
unempCentered = unemp - repmat(((max(unemp,[],2) - min(unemp,[],2)) / 2) + min(unemp,[],2),1, 4);
stayCentered = stay - repmat(((max(stay,[],2) - min(stay,[],2)) / 2) + min(stay,[],2),1, 4);

perCounty = zeros(size(unemp,1), 7);

% for each county, compute the cross-correlation and store them 
% to review the time-lag
for c = 1:size(stay,1)
    perCounty(c,:) = xcorr(stayCentered(c,:), unempCentered(c,:), 'coeff');
end

% plot the timelag for each county
plot(perCounty');

% Take the correlation at time-lag 0 and plot this on the counties
