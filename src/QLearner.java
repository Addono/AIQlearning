public class QLearner {  
    /**
     * This method performs Q-learning. TODO complete documentation.
     * @param rewards
     * @param paths
     * @param gamma
     * @param noIterations
     * @return 
     */
    public String execute(Integer[][] rewards, Integer[][] paths, Double gamma, Integer noIterations)
    {
        int numStates = rewards.length;
        int numActions = rewards[0].length;
        final Double[][] Q = new Double[numStates][numActions];
        
        // Initialize Q
        for (int s = 0; s < numStates; s++) {
            for (int a = 0; a < numActions; a++) {
                // Set Q to 0 if action a is applicable for state s, else let it
                // be null.
                Q[s][a] = rewards[s][a] == null ? null : 0d;
            }
        }
        
        // Do Q-learning
        for (int i = 0; i < noIterations; i++) { // For each iteration of Q-learning
            for (Integer[] path : paths) { // Apply Q-learning on every path
                execute(Q, rewards, path, gamma);
            }
        }
        
        return policy(Q);
    }
    
    /** do Q-learning for one path.  TODO complete documentation. */
    private void execute(Double[][] Q, Integer[][] rewards, Integer[] path, Double gamma) {
        // Set the initial state as the first index of the path.
        int s = path[0];
        
        // Loop through all subsequent actions in order.
        for (int i = 1; i < path.length; i++) {
            int sPrime = path[i]; // Set aPrime to the state resulting from taking a.
            
            // Let a' initially be null, update it with the index of the 
            // maximum Q value while iterating through the actions, if one is found.
            Integer aPrime = null;
            for (int action = 0; action < Q[sPrime].length; action++) {
                // Loop through all actions for this state, and update a'
                // if a better applicable action was encoutered.
                if (Q[sPrime][action] != null && (aPrime == null || Q[sPrime][action] > Q[sPrime][aPrime])) {
                    aPrime = action;
                }
            }
            
            // Check if a final state was reached in which no further applicable actions are present
            if (aPrime == null) {
                break;
            }
            
            // Else update Q if any of state s' has any applicable action.
            Q[s][sPrime] = (double) rewards[s][sPrime] + gamma * Q[sPrime][aPrime];
            s = sPrime;
        }
    }

    /**
     * Computes the policy.
     */
    private String policy(Double[][] Q) {
        // Initialize array to write the optimal action of each state to.
        String[] result = new String[Q.length];
        
        // Loop through all states
        for (int s = 0; s < Q.length; s++) {
            // Track the index of the best action found thus far. null indicates
            // no action which can be applied has been encountered (yet).
            Integer maxIndex = null;
            
            // Loop through all actions for this state, and update maxIndex
            // if a better applicable action was encoutered.
            for (int a = 0; a < Q[s].length; a++) {
                if (Q[s][a] != null && (maxIndex == null || Q[s][a] > Q[s][maxIndex])) {
                    maxIndex = a;
                }
            }
            
            // Convert the best applicable action index to a string.
            result[s] = maxIndex == null ? "n" : Integer.toString(maxIndex);
        }
        return String.join(" ", result); // Concatenate the optimal action of each state into a single space seperated line.
    }
}