
% Use cross correlation to see record the change over time of relevant
% health and job metrics
%
% author: chris keitel (ck1456@nyu.edu)
% contributed: chen price (cp1425@nyu.edu)

% skips the headers and county labels (for now)
data = csvread('combined-results.csv', 1, 1);

% Pull out
% Unemployment percent
unemp_percent = data(:, [4, 8, 12, 16]);
% Total unemployment
unemp = data(:, [3, 7, 11, 15]);
% Average stay days
avg_stay = data(:, [18, 29, 40, 51]);
% Average severity
avg_sev = data(:, [24, 35, 46, 57]);
% Total Charges
total_charges = data(:, [25, 36, 47, 58]);


% Need to center this data around 0
unemp_percent_centered = centerSignal(unemp_percent);
unemp_centered = centerSignal(unemp);
avg_stay_centered = centerSignal(avg_stay);
avg_sev_centered = centerSignal(avg_sev);
total_charges_centered = centerSignal(total_charges);

num_counties = size(unemp_percent_centered,1);


stayUnempCorr = zeros(num_counties, 7);
sevUnempCorr = zeros(num_counties, 7);
chargesUnempCorr = zeros(num_counties, 7);
chargesUnempPercentCorr = zeros(num_counties, 7);

% for each county, compute the cross-correlation and store them
% to review the time-lag
for c = 1:num_counties
    stayUnempCorr(c,:) = xcorr(avg_stay_centered(c,:), unemp_percent_centered(c,:), 'coeff');
    sevUnempCorr(c,:) = xcorr(avg_sev_centered(c,:), unemp_percent_centered(c,:), 'coeff');
    chargesUnempCorr(c,:) = xcorr(total_charges_centered(c,:), unemp_centered(c,:), 'coeff');
    chargesUnempPercentCorr(c,:) = xcorr(total_charges_centered(c,:), unemp_percent_centered(c,:), 'coeff');
end

% plot the timelag for each county
figure;
plot(stayUnempCorr');
title('Averge Stay correlated with Unemployment Percent');
figure;
plot(sevUnempCorr');
title('Averge Severity correlated with Unemployment Percent');
figure;
plot(chargesUnempCorr');
title('Total Charges correlated with Total Unemployment');
figure;
plot(chargesUnempPercentCorr');
title('Total Charges correlated with Unemployment Percent');

% Take the correlation at time-lag 0 and plot this on the counties
stayUnempCorr = stayUnempCorr(:,4);
sevUnempCorr = sevUnempCorr(:,4);
chargesUnempCorr = chargesUnempCorr(:,4);
chargesUnempPercentCorr = chargesUnempPercentCorr(:,4);

dataFile = fopen('combined-results.csv');
countyNames = textscan(dataFile, '%s %*[^\n]', 'delimiter', ',');
fclose(dataFile);

stay_unemp_map_file = fopen('stay_unemp_map.txt', 'w');
sev_unemp_map_file = fopen('sev_unemp_map.txt', 'w');
charges_unemp_map_file = fopen('charges_unemp_map.txt', 'w');
charges_unemp_percent_map_file = fopen('charges_unemp_percent_map.txt', 'w');
sev_unemp_map_green_file = fopen('sev_unemp_map_green.txt','w');
sev_unemp_map_blue_file = fopen('sev_unemp_map_blue.txt','w');

format short
for c = 1:num_counties
    name = lower(countyNames{1}{c+1});
    name = strrep(name, ' ', '_');
    name = strrep(name, 'allegheny', 'allegany');
    name = strrep(name, 'manhattan', 'new_york');
    
    fprintf(stay_unemp_map_file, '%s, %s\n', name, colorize(stayUnempCorr(c)));
    fprintf(sev_unemp_map_file, '%s, %s\n', name, colorize(sevUnempCorr(c)));
    fprintf(charges_unemp_map_file, '%s, %s\n', name, colorize(chargesUnempCorr(c)));
    fprintf(charges_unemp_percent_map_file, '%s, %s\n', name, colorize(chargesUnempPercentCorr(c)));
    
    % write county,correlation for counties positively correlated severity 
    if (sevUnempCorr(c) > 0)
        fprintf(sev_unemp_map_green_file, '%s, %.4f\n', name, sevUnempCorr(c));
    end
    
    % write county,correlation for counties negatively correlated severity 
    if (sevUnempCorr(c) < 0)
        fprintf(sev_unemp_map_blue_file, '%s, %.4f\n', name, sevUnempCorr(c));
    end
end

fclose(stay_unemp_map_file);
fclose(sev_unemp_map_file);
fclose(charges_unemp_map_file);
fclose(charges_unemp_percent_map_file);
fclose(sev_unemp_map_green_file);
fclose(sev_unemp_map_blue_file);




