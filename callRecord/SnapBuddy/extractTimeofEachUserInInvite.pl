#!/usr/bin/perl
use strict; # Always!
use warnings; # Always!
use Time::HiRes qw/gettimeofday/;

print "id,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10\n";
my $login = './login.sh devenmartinez@hotmail.com PS1Ljv4NPs';
$login = $login . $ARGV[0] . " " . $ARGV[1];

my $cmd = 'curl -s -L -b cookies.txt --insecure https://localhost:8080';

system($login);
# Authentication is also considered in call records...Let's assign random time to it

my $infile = 'pathOfAllPublicProfile.txt';
open (my $input, '<:encoding(UTF-8)', $infile) or die "Can't open to $infile: $!";
my $i = 0;
my $id = 1;
while (<$input>)
{
    $i = $i + 1 ;
    my($line) = $_;
    chomp $line;
    if($i % 2 eq 1)
    {
        print "$id,";
        $id += 1;
    }
    if($i % 2 eq 0)
    {
        for(my $k=0; $k < 10; $k++)
        {
            my $var1;
            my $var2;
            my $cmd1 = $cmd;
            $cmd1 = $cmd . $line . " > ./ss.jpg \n" ;
            
            my $seconds = gettimeofday();
            my $ms      = int($seconds*1000);
            
            system($cmd1);
            
            my $seconds1 = gettimeofday();
            my $ms1      = int($seconds1*1000);
            
            my $diff = $ms1 - $ms;
            if($k < 9){
                print "$diff,";
            }else{
                print "$diff\n";
            }
        }
    }
}
exit 0;
