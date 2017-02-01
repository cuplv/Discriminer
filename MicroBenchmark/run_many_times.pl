use Math::BigInt::Random::OO;
use strict;
use warnings;
use Time::HiRes qw/gettimeofday/;

my $class = $ARGV[0];
my @list = split('_', $class);
my $count = @list[1];
my $input;
my @list1 = split('\.', $class);
my $className = @list1[0];

my $filename = 'result_time.csv';
open(my $fh, '>', $filename) or die "Could not open file '$filename' $!";
print $fh "id,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10 \n";
my $id = 1;
if($class eq "LSB0_10_100_10" or $class eq "MSB0_10_100_10")
{
    foreach (1 .. 188) {
        use BigInt;
        my $r;
        $r = Math::BigInt->new(int CORE::rand 2**$count);
        $input = substr($r->as_bin(), 2, length $r->as_bin());
        my $command = "java -cp micro-benchmark.jar -javaagent:callRecord-1.jar microBenchmark_ver1." . $className . " " . $input;
        system($command);
        print $fh "$id,";
        for(my $k=0; $k < 10; $k++)
        {
            my $seconds = gettimeofday();
            my $ms      = int($seconds*1000);
            my $command = "java -cp micro-benchmark.jar microBenchmark_ver1." . $className . " " . $input;
            system($command);
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
}
else
{
    foreach (1 .. 10*$count) {
        use BigInt;
        my $r;
        $r = Math::BigInt->new(int CORE::rand 2**$count);
        $input = substr($r->as_bin(), 2, length $r->as_bin());
        my $command = "java -cp micro-benchmark.jar -javaagent:callRecord-1.jar microBenchmark_ver1." . $className . " " . $input;
        system($command);
        print $fh "$id,";
        for(my $k=0; $k < 10; $k++)
        {
            my $seconds = gettimeofday();
            my $ms      = int($seconds*1000);
            my $command = "java -cp micro-benchmark.jar microBenchmark_ver1." . $className . " " . $input;
            system($command);
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
}

