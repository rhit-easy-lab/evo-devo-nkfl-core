import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import pandas as pd
import statistics

from tkinter.filedialog import askopenfilename

#Read in our data from the file
file = askopenfilename()
df = pd.read_csv(file)

#Get the number of each generation
generationsAll = df.loc[:, 'Generation'].tolist()
generations = [*set(generationsAll)]

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

ax = plt.subplot()
ax.plot(xaxis, mean_values, label = "Final Fitness")
ax.fill_between(xaxis, lower_errors, upper_errors, alpha=0.25)


#Make our plot
plt.title('Final Fitnesses over Generational Time')
ax.set_xlabel('Generations')
ax.set_ylabel('Final Fitness of Best in Generation')

# Display our plot
plt.show()