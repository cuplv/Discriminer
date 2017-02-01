#!/usr/bin/perl
use strict; # Always!
use warnings; # Always!
use Time::HiRes qw/gettimeofday/;

print "id,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10\n";
my $id = 1;
for (my $i=1; $i <= 1; $i++) {
   for (my $j=0; $j < 4; $j++) {
       my $val = 0.02 * $i;
       my $arg2 = sprintf '%.2f',$val;
       my $command ='java -cp fix_user_pub_org.jar -javaagent:callRecord_gab_Feed_1.jar gab_feed1.gab_feed1' . ' 0 ' . $arg2 . ' 1';
       system($command);
       print "$id,";
       for(my $k=0; $k < 10; $k++)
       {
           my $seconds = gettimeofday();
           my $ms      = int($seconds*1000);
           
           my $command ='java -jar fix_user_pub_org.jar' . ' 0 ' . $arg2 . ' 1';
           system($command);
           my $seconds1 = gettimeofday();
           my $ms1      = int($seconds1*1000);
           
           my $diff = $ms1 - $ms;
           if($k < 9){
               print "$diff,";
           }else{
               print "$diff";
           }
       }
       print "\n";
       $id += 1;
	}
}
for (my $i=1; $i <= 1; $i++) {
    for (my $j=0; $j < 4; $j++) {
        my $val = 0.02 * $i;
        my $arg2 = sprintf '%.2f',$val;
        my $command ='java -cp fix_user_pub_org.jar -javaagent:callRecord_gab_Feed_1.jar gab_feed1.gab_feed1' . ' 15 ' . $arg2 . ' 1';
        system($command);
        print "$id,";
        for(my $k=0; $k < 10; $k++)
        {
            my $seconds = gettimeofday();
            my $ms      = int($seconds*1000);
            
            my $command ='java -jar fix_user_pub_org.jar' . ' 0 ' . $arg2 . ' 1';
            system($command);
            my $seconds1 = gettimeofday();
            my $ms1      = int($seconds1*1000);
            
            my $diff = $ms1 - $ms;
            if($k < 9){
                print "$diff,";
            }else{
                print "$diff";
            }
        }
        print "\n";
        $id += 1;
    }
}
