#!/usr/bin/perl
use strict; # Always!
use warnings; # Always!

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
#            print("ls -ltr $cur_line |  awk \'{print \$5}\'");
            system("ls -ltr $cur_line |  awk \'{print \$5}\'");
            system("mv $cur_line". "1 $cur_line");
        }
    }
}
exit 0;
