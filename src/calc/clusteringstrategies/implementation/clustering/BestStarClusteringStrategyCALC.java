package calc.clusteringstrategies.implementation.clustering;

import java.util.ArrayList;
import java.util.List;

import clusteringstrategies.implementation.clustering.BestStarClusteringStrategy;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;

public class BestStarClusteringStrategyCALC extends BestStarClusteringStrategy {
	private double threshold;

	public BestStarClusteringStrategyCALC(double threshold) {
		super(threshold);
		this.threshold = threshold;
	}

	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {

		// Auxiliary variables
		int dataSize = dataObjects.size();
		int numClusters = 0;

		DataCluster[] clusters = new DataCluster[dataSize];
		List<Integer> centers = new ArrayList<Integer>(); // store the indexes
															// of the centers

		// 1. Creates first cluster
		{
			for (int i = 0; i < dataSize; i++)
				clusters[i] = new DataCluster("Cluster " + i);

			DataCluster c = new DataCluster();
			c.addDataObject(dataObjects.get(0));
			clusters[0] = c;

			// The first center if the object with index 0 : OBJ0
			centers.add(0);
			numClusters++;
		}

		// 2. Create the other clusters
		for (int i = 1; i < dataSize; i++) {

			// Check the need for a new cluster
			boolean createNewCluster = true;
			for (int j = 0; j < numClusters; j++) {

				// The first object of a cluster will always be its center
				DataObject d = clusters[j].getObject(0);

				// If true, then the ith object can be assigned to a cluster, no
				// need for new cluster
				if (similarityMatrix.get(d.getIndex(), i) >= this.threshold) {
					createNewCluster = false;
					break;
				}
			}

			// If true, then the ith object could not be assigned to any cluster
			// : need for a new cluster
			if (createNewCluster) {
				DataCluster c = new DataCluster();
				c.addDataObject(dataObjects.get(i));
				clusters[numClusters] = c;

				centers.add(i);
				numClusters++;
			}
		}

		// 3. Populate clusters
		for (int i = 0; i < dataSize; i++) {

			// If true, then the ith object is a center
			if (centers.contains(i))
				continue;

			int index = -1;
			double max = -1;
			double sim = 0;

			for (int j = 0; j < numClusters; j++) {
				DataObject d = clusters[j].getObject(0);
				sim = similarityMatrix.get(d.getIndex(), i);

				if (sim > max) {
					max = sim;
					index = j;
				}
			}

			// Add object to cluster
			clusters[index].addDataObject(dataObjects.get(i));
		}

		// 4. Build DataClusters
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();
		for (int i = 0; i < numClusters; i++) {
			dataClusters.add(clusters[i]);
		}

		return dataClusters;
	}

}
