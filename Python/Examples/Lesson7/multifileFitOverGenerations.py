import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
from matplotlib import cm
import pandas as pd
import os
import statistics

from tkinter.filedialog import askopenfilename
from tkinter.filedialog  import askdirectory  

#Read in our data from the file
directory = askdirectory()
dfs = []
filenames = []
for file in sorted(os.listdir(directory)):
    filename = os.fsdecode(file)
    if filename.endswith(".csv"): 
        filenames.append(filename[0:len(filename)-4])
        dfs.append(pd.read_csv(directory + "/" + file))

#Get the number of each generation (number of generations must match for this to work)
generationsAll = dfs[0].loc[:, 'Generation'].tolist()
generations = [*set(generationsAll)]

#Lets us make a range of colors for the different files
colors = []
for i in range(len(dfs)):
    colors.append(cm.CMRmap(i/len(dfs)))

ax = plt.subplot()

index = 0 #Lets us keep track of filenames
for df in dfs:
    #Calculate the mean values and error of the fitness at each generation
    plot_error = "standard_error"
    mean_values = []
    lower_errors = []
    upper_errors = []
    xaxis = generations

    for gen in generations:
        gendata = df.loc[df['Generation'] == gen]
        genfits = gendata.loc[:,'Final_Fitness'].tolist()
        
        mean = statistics.mean(genfits)
        mean_values.append(mean)

        std = statistics.stdev(genfits)
        if plot_error == "standard_error":
            error = std/np.sqrt(len(genfits))
            lower_errors.append( mean - error )
            upper_errors.append( mean + error )
        elif plot_error == "standard_deviation":
            lower_errors.append( mean - std )
            upper_errors.append( mean + std )
        else:
            print("plot_error must be standard_error or standard_deviation")
            exit(1)

    ax.plot(xaxis, mean_values, color = colors[index], label = filenames[index])
    ax.fill_between(xaxis, lower_errors, upper_errors, alpha=0.25, color = colors[index])

    index = index + 1


#Make our plot
plt.title('Final Fitnesses over Generational Time')
ax.set_xlabel('Generations')
ax.set_ylabel('Final Fitness of Best in Generation')

handles, labels = ax.get_legend_handles_labels()
labels, handles = zip(*sorted(zip(labels, handles), key=lambda t: t[0]))
ax.legend(handles, labels, loc = 'upper right')

# Display our plot
plt.show()