#!/usr/bin/perl
use strict; # Always!
use warnings; # Always!
use Time::HiRes qw/gettimeofday/;

my $filename = 'result_time.csv';
open(my $fh, '>', $filename) or die "Could not open file '$filename' $!";
print $fh "id,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10\n";
my $id = 1;
foreach (0 .. 191) {
    my $new_i = sprintf("%d", $id);
    my $command = "./bin/textcrunchrhost_3_callRecord" . " Data/" . $new_i . ".txt";
    system($command);
    print $fh "$id,";
    for(my $k=0; $k < 10; $k++)
    {
       my $seconds = gettimeofday();
       my $ms      = int($seconds*1000);
       
       my $command_1 = "./bin/textcrunchrhost_3" . " Data/" . $new_i . ".txt";
       system($command_1);
       my $seconds1 = gettimeofday();
       my $ms1      = int($seconds1*1000);
       
       my $diff = $ms1 - $ms;
       if($k < 9){
           print $fh "$diff,";
       }else{
           print $fh "$diff";
       }
    }
    print $fh "\n";
    $id += 1;
}
