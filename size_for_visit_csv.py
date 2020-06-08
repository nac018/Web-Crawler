import csv

very_small = 0
small = 0
normal = 0
large = 0
very_large = 0

with open('visit_foxnews.csv') as csv_file:
    csv_reader = csv.reader(csv_file)
    for i,row in enumerate(csv_reader):
        if i == 0:
            continue
        if int(row[1]) < 1024:
            very_small += 1
        elif int(row[1]) >= 1024 and int(row[1]) < 10240:
            small += 1
        elif int(row[1]) >= 10240 and int(row[1]) < 102400:
            normal += 1
        elif int(row[1]) >= 102400 and int(row[1]) < 1048576:
            large += 1
        elif int(row[1]) >= 1048576:
            very_large += 1

    print("very_small: " + str(very_small))
    print("small: " + str(small))
    print("normal: " + str(normal))
    print("large: " + str(large))
    print("very_large: " + str(very_large))
    print("done!!!!!!")