import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import pandas as pd

from tkinter.filedialog import askopenfilename

#Read in our data from the file
file = askopenfilename()
df = pd.read_csv(file)

#Get out final fitnesses
fitnesses = df.loc[:,'Final_Fitness'].tolist()

#Make our plot
plt.title('Final Fitnesses over Generational Time')
ax = plt.subplot()
ax.set_xlabel('Generations')
ax.set_ylabel('Final Fitness of Best in Generation')
plt.plot(fitnesses)

# Display our plot
plt.show()