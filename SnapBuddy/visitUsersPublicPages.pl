#!/usr/bin/perl
use strict; # Always!
use warnings; # Always!
use Time::HiRes qw/gettimeofday/;

my $login = './login.sh devenmartinez@hotmail.com PS1Ljv4NPs';
$login = $login . $ARGV[0] . " " . $ARGV[1];

my $cmd = 'curl -s -L -b cookies.txt --insecure https://localhost:8080';

system($login);

my $infile = 'pathOfAllPublicProfile.txt';
open (my $input, '<:encoding(UTF-8)', $infile) or die "Can't open to $infile: $!";
my $i = 0;
while (<$input>)
{
    $i = $i + 1 ;
    my($line) = $_;
    chomp $line;
    if($i % 2 eq 1)
    {
        print "Downloading public profile of $line \n";
    }
    if($i % 2 eq 0)
    {
        my $var1;
        my $var2;
        my $cmd1 = $cmd;
        $cmd1 = $cmd . $line . " > ./ss.jpg \n" ;
        system($cmd1);
    }
}
exit 0;
