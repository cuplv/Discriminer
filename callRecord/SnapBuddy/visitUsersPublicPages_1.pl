#!/usr/bin/perl
use strict; # Always!
use warnings; # Always!
use Time::HiRes qw/gettimeofday/;

my $login = './login.sh devenmartinez@hotmail.com PS1Ljv4NPs';
$login = $login . $ARGV[0] . " " . $ARGV[1];

my $cmd = 'curl -s -L -b cookies.txt --insecure https://localhost:8080';

system($login);

my $infile = 'pathOfAllPublicProfile_1.txt';
for(my $j = 5; $j <= 105 ; $j++)
{
    open (my $input, '<:encoding(UTF-8)', $infile) or die "Can't open to $infile: $!";
    my $i = 0;
    my $cur_line;
    while (<$input>)
    {
        $i = $i + 1 ;
        my($line) = $_;
        chomp $line;
        if($i % 2 eq 1)
        {
            system("cp $line $line". "1");
            system("convert $line -resize $j"."% $line");
            $cur_line = $line;
        }
        if($i % 2 eq 0)
        {
            my $var1;
            my $var2;
            my $cmd1 = $cmd;
            $cmd1 = $cmd . $line . " > ./ss.jpg \n" ;
            system($cmd1);
            system("mv $cur_line". "1 $cur_line");
        }
    }
}
exit 0;
