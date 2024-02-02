package chess.Logic;

import java.util.*;
import java.util.concurrent.*;

public class AI {
    public static GameUpdate gameUpdate = Game.gameUpdate;
    public static int depth;
    public static int currentDeepness;
    public static boolean remove;
    public static ArrayList<String[][]> possibleMoves;
    public static CopyOnWriteArrayList<String[][]>  castledPositionsAI;
    public static CopyOnWriteArrayList<String[][]>  castledPositionsPlayer;
    public static String[][] startingPosition;
    static final Object lock = new Object();

    public static int[] calculateMove(String[][] pieces) {
        gameUpdate = Game.gameUpdate;                   // update GameUpdate object
        startingPosition = pieces;

        depth = Settings.difficulty;                    // depth of the algorithm
        remove = false;

        castledPositionsAI = new CopyOnWriteArrayList<>();
        castledPositionsPlayer = new CopyOnWriteArrayList<>();

        checkIfCastled();
        possibleMoves = getAllMoves(pieces, 1);       // get list with all pieces that are able to move

        return manageCalculating(depth, possibleMoves);

    }

    /**
     * Transforms a list of moves to a list of pieces
     *
     * @param moves
     * @param pieces
     * @return
     */
    private static ArrayList<String[][]> moveToBoard(ArrayList<int[]> moves, String[][] pieces, int currentColor) {

        ArrayList<String[][]> list = new ArrayList<>();

        for (int[] move : moves) {
            String[][] piecesCopy = gameUpdate.deepCopy(pieces);
            if ((gameUpdate.getType(move[0], move[1], pieces) == 11 || gameUpdate.getType(move[0], move[1], pieces) == 10) && (move[2] == 0 || move[2] == 7)) {
                list.addAll(promotionAI(move, pieces));
            }
            else if((gameUpdate.getType(move[0], move[1], pieces) == 11 || gameUpdate.getType(move[0], move[1], pieces) == 10) && move[1] != move[3] && pieces[move[2]][move[3]] == null){

                piecesCopy[move[2]][move[3]] = piecesCopy[move[0]][move[1]];
                piecesCopy[move[0]][move[1]] = null;

                piecesCopy[move[0]][move[3]] = null;



                list.add(piecesCopy);
            }
            else if(isCastle(pieces, move)){
                if(gameUpdate.front){
                    if(move[3] == 2){
                        piecesCopy[move[2]][move[3]] = piecesCopy[move[0]][move[1]];
                        piecesCopy[move[0]][move[1]] = null;
                        piecesCopy[move[0]][move[1]-1] = piecesCopy[move[0]][0];
                        piecesCopy[move[0]][0] = null;
                    }
                    else{
                        piecesCopy[move[2]][move[3]] = piecesCopy[move[0]][move[1]];
                        piecesCopy[move[0]][move[1]] = null;
                        piecesCopy[move[0]][move[1]+1] = piecesCopy[move[0]][7];
                        piecesCopy[move[0]][7] = null;
                    }
                }
                else{
                    if(move[3] == 5){
                        piecesCopy[move[2]][move[3]] = piecesCopy[move[0]][move[1]];
                        piecesCopy[move[0]][move[1]] = null;
                        piecesCopy[move[0]][move[1]+1] = piecesCopy[move[0]][7];
                        piecesCopy[move[0]][7] = null;
                    }
                    else{
                        piecesCopy[move[2]][move[3]] = piecesCopy[move[0]][move[1]];
                        piecesCopy[move[0]][move[1]] = null;
                        piecesCopy[move[0]][move[1]-1] = piecesCopy[move[0]][0];
                        piecesCopy[move[0]][0] = null;
                    }
                }
                synchronized (lock){
                    if(currentColor == gameUpdate.currentPlayer){
                        castledPositionsAI.add(piecesCopy);
                    }
                    else{
                        castledPositionsPlayer.add(piecesCopy);
                    }
                }
                list.add(piecesCopy);
            }
            else{
                piecesCopy[move[2]][move[3]] = piecesCopy[move[0]][move[1]];
                piecesCopy[move[0]][move[1]] = null;

                list.add(piecesCopy);
            }
            synchronized (lock){
                for (String[][] arr : castledPositionsAI) {
                    if (Arrays.deepEquals(arr, pieces)){
                        castledPositionsAI.add(piecesCopy);
                        break;
                    }
                }
                for (String[][] arr : castledPositionsPlayer) {
                    if (Arrays.deepEquals(arr, pieces)){
                        castledPositionsPlayer.add(piecesCopy);
                    }
                }
            }
        }
        return list;
    }

    private static boolean isCastle(String[][] pieces, int[] move) {
        return (gameUpdate.getType(move[0], move[1], pieces) == 1 || gameUpdate.getType(move[0], move[1], pieces) == 0) &&
                (move[1] == 4 && (move[3] == 6 || move[3] == 2) ||
                        (move[1] == 3 && (move[3] == 5 || move[3] == 1)));
    }

    private static Collection<? extends String[][]> promotionAI(int[] move, String[][] pieces) {
        ArrayList<String[][]> list = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            list.add(gameUpdate.deepCopy(pieces));
        }
        int color = gameUpdate.getColor(move[0], move[1], pieces);

        if (color == 1) {
            list.get(0)[move[2]][move[3]] = "2" + color;
        } else {
            list.get(0)[move[2]][move[3]] = "3" + color;
        }
        list.get(0)[move[0]][move[1]] = null;
        if (color == 1) {
            list.get(1)[move[2]][move[3]] = "8" + color + "1";
        } else {
            list.get(1)[move[2]][move[3]] = "9" + color + "1";
        }
        list.get(1)[move[0]][move[1]] = null;
        if (color == 1) {
            list.get(2)[move[2]][move[3]] = "6" + color;
        } else {
            list.get(2)[move[2]][move[3]] = "7" + color;
        }
        list.get(2)[move[0]][move[1]] = null;
        if (color == 1) {
            list.get(3)[move[2]][move[3]] = "4" + color;
        } else {
            list.get(3)[move[2]][move[3]] = "5" + color;
        }
        list.get(3)[move[0]][move[1]] = null;

        return list;
    }

    private static ArrayList<String[][]> getAllMoves(String[][] pieces, int deepness) {
        int currentColor;

        if (deepness % 2 == 0) {
            if (gameUpdate.currentPlayer == 1) {
                currentColor = 0;
            } else {
                currentColor = 1;
            }
        } else {
            currentColor = gameUpdate.currentPlayer;
        }

        ArrayList<int[]> moves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] != null && gameUpdate.getColor(i, j, pieces) == currentColor) {
                    ArrayList<Integer> list = gameUpdate.possibleMovesAI(pieces, i, j, currentColor);

                    if (!list.isEmpty()) {
                        for (int k = 0; k < list.size(); k += 2) {
                            moves.add(new int[]{i, j, list.get(k), list.get(k + 1)});
                        }
                    }
                }
            }
        }
        return moveToBoard(moves, pieces, currentColor);
    }

    private static int getPlayerColor(int color) {
        if (color == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int[] manageCalculating(int depth, ArrayList<String[][]> movablePieces) {
        //long time = System.currentTimeMillis();

        ExecutorService threadPool = Executors.newCachedThreadPool();

        ArrayList<Callable<ArrayList<ArrayList<String[][]>>>> callables = new ArrayList<>();
        ArrayList<ArrayList<String[][]>> lastQueue = new ArrayList<>(movablePieces.size());
        ArrayList<ArrayList<ArrayList<String[][]>>> temp = new ArrayList<>(movablePieces.size());
        ArrayList<ArrayList<String[][]>> moves = new ArrayList<>(movablePieces.size());

        int amountOfMoves = movablePieces.size();

        for (int deep = 1; deep < depth+1; deep+=2) {
            moves = new ArrayList<>(movablePieces.size());
            for (int i = 0; i < amountOfMoves; i++) {
                if(deep != 1 && lastQueue.get(i) == null){
                    Callable<ArrayList<ArrayList<String[][]>>> task = () -> {
                        return null;
                    };
                    callables.add(task);
                    continue;
                }
                final int index = i;
                final int finalDeep = deep;
                final ArrayList<String[][]> currentLastQueue;

                        if(deep == 1){
                            currentLastQueue = null;
                        }
                        else{
                            currentLastQueue = lastQueue.get(index);;
                        }
                Callable<ArrayList<ArrayList<String[][]>>> task = () -> {
                    Queue<String[][]> queue = new LinkedList<>();
                    if (finalDeep == 1) {
                        queue.add(movablePieces.get(index));
                    } else {
                        queue.addAll(currentLastQueue);
                    }
                    return calculate(queue, new ArrayList<>(), finalDeep, finalDeep+2, index);
                };
                callables.add(task);
            }

            try {
                //long time3 = System.currentTimeMillis();
                List<Future<ArrayList<ArrayList<String[][]>>>> futures =  threadPool.invokeAll(callables);
                for (int i = 0; i < amountOfMoves; i++) {
                    temp.add(i, futures.get(i).get());
                    if(temp.get(i) == null){
                        if(deep == 1){
                            lastQueue.add(i, null);
                        }
                        else{
                            lastQueue.set(i, null);
                        }
                    }
                    else {
                        if (deep == 1) {
                            lastQueue.add(i, temp.get(i).get(1));
                        } else {
                            lastQueue.set(i, temp.get(i).get(1));
                        }
                    }
                    if(temp.get(i) == null){
                        moves.add(i, null);
                    }
                    else{
                        moves.add(i,temp.get(i).get(0));
                    }
                }
                //System.out.println("Time getFutures: "+(System.currentTimeMillis() - time3)) ;
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            if (remove) {

                //long time2 = System.currentTimeMillis();
                ArrayList<ArrayList<ArrayList<String[][]>>> newMoves = removeMoves(moves, lastQueue, deep);
                moves = newMoves.get(0);
                lastQueue = newMoves.get(1);
                amountOfMoves = moves.size();
            }
            remove = false;
            callables.clear();
            temp = new ArrayList<>(amountOfMoves);
        }
        //System.out.println("Time calculate: "+(System.currentTimeMillis() - time)) ;

//            moves = new ArrayList<>(movablePieces.size());
//            for (int i = 0; i < amountOfMoves; i++) {
//                final int index = i;
//
//
//                Callable<ArrayList<ArrayList<String[][]>>> task = () -> {
//                    Queue<String[][]> queue = new LinkedList<>();
//
//                        queue.add(movablePieces.get(index));
//
//                    return calculate(queue, new ArrayList<>(), 1, depth );
//                };
//                callables.add(task);
//            }
//
//
//            try {
//                List<Future<ArrayList<ArrayList<String[][]>>>> futures =  threadPool.invokeAll(callables);
//                for (int i = 0; i < amountOfMoves; i++) {
//                    temp.add(i, futures.get(i).get());
//
//                    moves.add(i,temp.get(i).get(0));
//                }
//            } catch (InterruptedException | ExecutionException e) {
//                throw new RuntimeException(e);
//            }
//





//
//        ArrayList<ArrayList<String[][]>> lastQueue = new ArrayList<>(movablePieces.size());
//        ArrayList<ArrayList<ArrayList<String[][]>>> temp = new ArrayList<>(movablePieces.size());
//        HashMap<Integer, Callable<ArrayList<ArrayList<String[][]>>>> tasks = new HashMap<>();
//
//        for (int i = 0; i < movablePieces.size(); i++) {
//            moves.add(null);
//            lastQueue.add(null);
//            temp.add(null);
//        }
//
////        ArrayList<Callable<ArrayList<ArrayList<String[][]>>>> callables = new ArrayList<>();
//        ExecutorService threadPool = Executors.newCachedThreadPool();
//        CompletionService<ArrayList<ArrayList<String[][]>>> completionService = new ExecutorCompletionService<>(threadPool);
//
//        for (int deep = 0; deep < depth; deep+=3) {
//
//            for (int i = 0; i < movablePieces.size(); i++) {
//                final int index = i;
//                int finalDeep = deep;
//
//                Callable<ArrayList<ArrayList<String[][]>>> task = () -> {
//                    Queue<String[][]> queue = new LinkedList<>();
//                    if(finalDeep == 0){
//                        queue.add(movablePieces.get(index));
//                    }
//                    else{
//                        queue.addAll(lastQueue.get(index));
//                    }
//                    return calculate(queue, new ArrayList<>(), finalDeep+1, finalDeep + 3);
//                };
//
//                tasks.put(i, task);
//                completionService.submit(tasks.get(i));
//
//            }
//
//
//            for (int i = 0; i < movablePieces.size(); i++) {
//                try {
//                    temp.set(i, completionService.take().get());
//                    if(deep == 0){
//                        moves.set(i,temp.get(i).get(0));
//                    }
//                    else{
//                        moves.get(i).addAll(temp.get(i).get(0));
//                    }
//
//                    lastQueue.set(i, temp.get(i).get(1));
//                } catch (InterruptedException | ExecutionException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            System.out.println(moves.size());
//            System.out.println(lastQueue.size());
//        }

        String[][] move = selectMove(moves);

        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;

        if(iscastled(startingPosition, move)){
            int[] startKing = getKing(startingPosition, gameUpdate.currentPlayer);
            int[] endKing = getKing(move, gameUpdate.currentPlayer);

            return new int[]{endKing[0], endKing[1], startKing[0], startKing[1]};
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (move[i][j] == null && startingPosition[i][j] != null && gameUpdate.getColor(i, j, startingPosition) == gameUpdate.currentPlayer) {
                    x2 = i;
                    y2 = j;
                } else if (!Objects.equals(move[i][j], startingPosition[i][j])) {
                    x1 = i;
                    y1 = j;
                }
            }
        }

        int[] result = {x1, y1, x2, y2};

        if(gameUpdate.front != gameUpdate.frontAI){
            result = gameUpdate.rotateArr(result);
        }


        return result;
    }

    private static void checkIfCastled() {
        synchronized (lock){
            if((gameUpdate.currentPlayer == 1 && gameUpdate.hasCastledPlayer1) || (gameUpdate.currentPlayer == 0 && gameUpdate.hasCastledPlayer0)){
                castledPositionsAI.add(startingPosition);
            }
            else if((gameUpdate.currentPlayer == 0 && gameUpdate.hasCastledPlayer1) || (gameUpdate.currentPlayer == 1 && gameUpdate.hasCastledPlayer0)){
                castledPositionsPlayer.add(startingPosition);
            }
        }
    }

    private static boolean iscastled(String[][] startingPosition, String[][] move) {
        int[] startKing = getKing(startingPosition, gameUpdate.currentPlayer);
        int[] endKing = getKing(move, gameUpdate.currentPlayer);

        if(2 - (Math.abs(startKing[1] - endKing[1])) == 0){
            return true;
        }
        return false;
    }

    private static ArrayList<ArrayList<ArrayList<String[][]>>> removeMoves(ArrayList<ArrayList<String[][]>> moves, ArrayList<ArrayList<String[][]>> lastQueue, int depth) {
//long time2 = System.currentTimeMillis();
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ArrayList<Callable<Double>> callables = new ArrayList<>();

        ArrayList<Double> ratings = new ArrayList<>();

        final double removeFactor;

        if(depth == 1){
            removeFactor = 0.6;
        }
        else if(depth == 3){
            removeFactor = 0.7;
        } else if(depth == 7){
            removeFactor = 0.8;
        }
        else{
            removeFactor = 0.9;
        }

        for (int i = 0; i < moves.size(); i++) {
            final int index = i;
            Callable<Double> task = () ->{
                ArrayList<Double> ratingMoves = comparePosition(moves.get(index), getCurrentColor(currentDeepness));

                double counter = 0.0;

                for(double num : ratingMoves){
                    counter += num;
                }
                return counter/ratingMoves.size();
            };
            callables.add(task);
        }

        try {
            List<Future<Double>> futures = threadPool.invokeAll(callables);
            for(int i = 0; i < moves.size(); i++){
                ratings.add(i, futures.get(i).get());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        threadPool.shutdown(); // Initiates an orderly shutdown

        int amountOfMovesToRemove = (int) (ratings.size()*removeFactor);

        for (int i = 0; i < amountOfMovesToRemove; i++) {
            int indexOfLowestMove = ratings.indexOf(Collections.min(ratings, Comparator.nullsLast(Comparator.naturalOrder())));

            ratings.set(indexOfLowestMove, null);
            moves.set(indexOfLowestMove, null);
            lastQueue.set(indexOfLowestMove, null);
        }
        ArrayList<ArrayList<ArrayList<String[][]>>> list = new ArrayList<>();
        list.add(0, moves);
        list.add(1, lastQueue);
        //System.out.println("Time remove: "+(System.currentTimeMillis() - time2)) ;
        return list;
    }

    private static int getCurrentColor(int currentDeepness) {
        if (currentDeepness % 2 == 0) {
            if (gameUpdate.currentPlayer == 1) {
                return 0;
            } else {
                return  1;
            }
        } else {
            return gameUpdate.currentPlayer;
        }
    }


    /**
     * Calculates all possible moves and positions to a specific deepness
     *
     * @param queue
     * @param moves
     * @param deepness
     * @return
     */
//    private static ArrayList<String[][]> calculate(Queue<String[][]> queue,
//                                                   ArrayList<String[][]> moves,
//                                                   int deepness) {
//        //TODO
//        // logging and improving
//
//        if (queue.isEmpty() || deepness >= depth) {
//            moves.addAll(queue);
//            return moves; // all moves calculated
//        }
//        deepness++;
//        Queue<String[][]> tempQueue = new LinkedList<>(); // create a new Queue where all moves are put in
//
//        while (!queue.isEmpty()) {
//            ArrayList<String[][]> allMoves = getAllMoves(queue.peek(), deepness); // put all possible moves of player in array
//            ArrayList<String[][]> list = selectMoves(allMoves, deepness);
//            tempQueue.addAll(list);
//
//            moves.add(queue.peek()); // add peek to final arraylist
//            queue.remove(); // remove peek element of queue (if queue is empty then the depth is completed)
//        }
//
//        queue = tempQueue; // queue = tempQueue because we want to pass a full queue to the calculate method
//
//
//        return calculate(queue, moves, deepness);
//
//    }
    private static ArrayList<ArrayList<String[][]>> calculate(Queue<String[][]> queue,
                                                   ArrayList<String[][]> moves,
                                                   int deepness, int maxDepth, int move) {
        //TODO
        // logging and improving
        // 2;3

//        synchronized (lock){
//            System.out.println("deepness:"+deepness);
//            System.out.println("quque: "+queue.size());
//            System.out.println("moves: "+moves.size());
//        }

        if(move == 10){
            deepness--;
            deepness++;
        }

        if (queue.isEmpty() || deepness >= depth || (deepness >= maxDepth)) {
            if(!queue.isEmpty() && deepness >= maxDepth && depth != maxDepth){
                remove = true;
            }
            else{
                moves.addAll(queue);
            }
            if(!queue.isEmpty()){
                currentDeepness = deepness;
            }

            ArrayList<ArrayList<String[][]>> list = new ArrayList<>();
            list.add(0, moves);
            list.add(1,  new ArrayList<>(queue));
            return list; // all moves calculated
        }
        deepness++;
        Queue<String[][]> tempQueue = new LinkedList<>(); // create a new Queue where all moves are put in

        while (!queue.isEmpty()) {
            //long time3 = System.currentTimeMillis();
            ArrayList<String[][]> allMoves = getAllMoves(queue.peek(), deepness); // put all possible moves of player in array
            ArrayList<String[][]> list = selectMoves(allMoves, deepness, false);
            //System.out.println("Time select: "+(System.currentTimeMillis() - time3)) ;
            tempQueue.addAll(list);

            if(!(deepness >= depth) && deepness >= maxDepth){
                moves.add(queue.peek()); // add peek to final arraylist
            }
            queue.remove(); // remove peek element of queue (if queue is empty then the depth is completed)
        }

        queue = tempQueue; // queue = tempQueue because we want to pass a full queue to the calculate method


        return calculate(queue, moves, deepness, maxDepth, move);

    }

    private static String[][] selectMove(ArrayList<ArrayList<String[][]>> moves) {
        //TODO
        // problem

        ArrayList<Double> avgRating = new ArrayList<>(Collections.nCopies(moves.size(), null));

        final int maxMoves = 1;
        final double maxDiff;

        switch(depth){
            case 1:
                maxDiff = 0.2;
                break;
            case 2:
                maxDiff = 0.175;
                break;
            case 3:
                maxDiff = 0.15;
                break;
            case 4:
                maxDiff = 0.125;
                break;
            case 5:
                maxDiff = 0.1;
                break;
            case 6:
                maxDiff = 0.075;
                break;
            case 7:
                maxDiff = 0.05;
                break;
            case 8:
                maxDiff = 0.04;
                break;
            case 9:
                maxDiff = 0.03;
                break;
            case 10:
                maxDiff = 0.02;
                break;
            default:
                throw new RuntimeException();
        }
        //long time = System.currentTimeMillis();

        int index = -1;

        for (ArrayList<String[][]> list : moves) {
            index++;
            if(list == null){
                avgRating.set(index, null);
                continue;
            }
            ArrayList<Double> ratingMoves = comparePosition(list, getCurrentColor(currentDeepness));
            //sort(ratingMoves, list, 0, ratingMoves.size()-1);

            double avg = 0.0;
            for (double rating : ratingMoves) {
                avg += rating;
            }
            avgRating.set(index, avg/ratingMoves.size());
        }

        ArrayList<String[][]> selectedMoves = new ArrayList<>();
        double max = Collections.max(avgRating, Comparator.nullsFirst(Comparator.naturalOrder()));
        selectedMoves.add(possibleMoves.get(avgRating.indexOf(max)));
        avgRating.set(avgRating.indexOf(max), null);

        for(int i = 0; i < maxMoves-1; i++){
            double tempMax = Collections.max(avgRating, Comparator.nullsFirst(Comparator.naturalOrder()));
            if(!avgRating.isEmpty() && max - tempMax <= maxDiff){
                selectedMoves.add(possibleMoves.get(avgRating.indexOf(tempMax)));
                avgRating.set(avgRating.indexOf(tempMax), null);
            }
            else{
                break;
            }
        }
        //System.out.println("Time selectMove: "+(System.currentTimeMillis() - time)) ;

        Random random = new Random();
        return selectedMoves.get(random.nextInt(selectedMoves.size()));
    }

    private static ArrayList<String[][]> selectMoves(ArrayList<String[][]> moves, int depth, boolean isLast) {

        boolean isMax;

       if(depth % 2 == 0 || isLast){
           isMax = false;
       }
       else{
           isMax = true;
       }


        ArrayList<Double> ratingMoves = comparePosition(moves, depth);
        sort(ratingMoves, moves, 0, ratingMoves.size() - 1);

        ArrayList<String[][]> remainingMoves = new ArrayList<>();

        final int amountOfMoves;
        if(!isMax){
            amountOfMoves = 1;
        }
        else if(depth <= 4){
            amountOfMoves = 3;
        } else if (depth <= 7) {
            amountOfMoves = 2;
        }
        else{
            amountOfMoves = 1;
        }

        if(!isMax){
            Collections.reverse(moves);
            Collections.reverse(ratingMoves);
        }


        for (String[][] move : moves) {
            if (remainingMoves.size() < amountOfMoves) {
                remainingMoves.add(move);
            } else {
                break;
            }
        }
        return remainingMoves;
    }

    private static void sort(ArrayList<Double> ratingMoves, ArrayList<String[][]> list, int low, int high) {

        if (low < high) {
            int pivot = high;
            int i = low - 1;

            for (int j = low; j < high; j++) {
                if (ratingMoves.get(j) > ratingMoves.get(pivot)) {
                    i++;
                    double temp = ratingMoves.get(i);
                    ratingMoves.set(i, ratingMoves.get(j));
                    ratingMoves.set(j, temp);

                    String[][] temp1 = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp1);
                }
            }
            double temp = ratingMoves.get(pivot);
            ratingMoves.set(pivot, ratingMoves.get(i + 1));
            ratingMoves.set(i + 1, temp);

            String[][] temp1 = list.get(pivot);
            list.set(pivot, list.get(i + 1));
            list.set(i + 1, temp1);

            pivot = i + 1;

            sort(ratingMoves, list, low, pivot - 1);
            sort(ratingMoves, list, pivot + 1, high);
        }
    }

    /**
     * Compare multiple positions
     *
     * @param positions Array with comparable positions
     * @return sorted ArrayList based on the position
     */
    private synchronized static ArrayList<Double> comparePosition(ArrayList<String[][]> positions, int currentPlayer) {

        int aiColor = gameUpdate.currentPlayer;
        int playerColor = getPlayerColor(aiColor);

        ArrayList<Double> ratings = new ArrayList<>();

        for (String[][] position : positions) {

            double rating = 0.0;
            boolean isRemis = false;

            // check if end
            switch (isEnd(aiColor, position)) {
                case 0:
                    if (isEnd(playerColor, position) == 1) {
                        rating = 50.0;
                    } else {
                        rating = 0.5;
                    }
                    break;
                case 1:
                    rating = -49.0;
                    break;
                case 2:
                    isRemis = true;
                    break;
            }
            if (rating == 50.0 || rating == -49.0) {
                ratings.add(rating);
                continue;
            }

            // check the material difference
            final double ratingPerMaterial = 10;
            int materialDiff = compareMaterial(position, aiColor);

            if (materialDiff > 0) {
                rating += materialDiff * ratingPerMaterial;
            } else if (materialDiff < 0) {
                rating += materialDiff * ratingPerMaterial;
            }

            // check safety of king
            int[] fields = {-1, -1, -1, 0, -1, 1, 0, 1, 1, 1, 1, 0, 1, -1, 0, -1, 0, 0};

            int[] aiKing = getKing(position, aiColor);
            int[] playerKing = getKing(position, playerColor);

            double aiKingRating = 0.0;
            double playerKingRating = 0.0;

            for (int i = 0; i < fields.length; i += 2) {
                if (aiKing[0] + fields[i] >= 0 && aiKing[0] + fields[i] <= 8 && aiKing[1] + fields[i + 1] >= 0 && aiKing[1] + fields[i + 1] <= 8) {
                    ArrayList<Integer> attackingPiecesAI = isAttacked(position, aiColor, aiKing[0] + fields[i], aiKing[1] + fields[i + 1]);
                    ArrayList<Integer> attackingPiecesPlayer = isAttacked(position, playerColor, aiKing[0] + fields[i], aiKing[1] + fields[i + 1]);

                    if (fields[i] == 0 && fields[i + 1] == 0) {
                        aiKingRating += getKingSafety(attackingPiecesAI, attackingPiecesPlayer, aiColor, true);
                    } else {
                        aiKingRating += getKingSafety(attackingPiecesAI, attackingPiecesPlayer, aiColor, false);
                    }
                }
                if (playerKing[0] + fields[i] >= 0 && playerKing[0] + fields[i] <= 8 && playerKing[1] + fields[i + 1] >= 0 && playerKing[1] + fields[i + 1] <= 8) {
                    ArrayList<Integer> attackingPiecesAI = isAttacked(position, aiColor, playerKing[0] + fields[i], playerKing[1] + fields[i + 1]);
                    ArrayList<Integer> attackingPiecesPlayer = isAttacked(position, playerColor, playerKing[0] + fields[i], playerKing[1] + fields[i + 1]);

                    if (fields[i] == 0 && fields[i + 1] == 0) {
                        playerKingRating += getKingSafety(attackingPiecesAI, attackingPiecesPlayer, playerColor, true);
                    } else {
                        playerKingRating += getKingSafety(attackingPiecesAI, attackingPiecesPlayer, playerColor, false);
                    }
                }
            }

            rating += aiKingRating - playerKingRating;


            // check the development in the position

            int lightPiecesAI = 0;
            int heavyPiecesAI = 0;
            int lightPiecesPlayer = 0;
            int heavyPiecesPlayer = 0;
            int pawn = 0;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (position[i][j] != null && (gameUpdate.getType(i, j, position) == 4 || gameUpdate.getType(i, j, position) == 5 || gameUpdate.getType(i, j, position) == 6 || gameUpdate.getType(i, j, position) == 7)) {
                        if (gameUpdate.getColor(i, j, position) == aiColor) {
                            ArrayList<Integer> moveList = gameUpdate.possibleMovesAI(position, i, j, aiColor);
                            lightPiecesAI += (moveList.size() / 2);
                        } else {
                            ArrayList<Integer> moveList = gameUpdate.possibleMovesAI(position, i, j, playerColor);
                            lightPiecesPlayer += (moveList.size() / 2);
                        }
                    } else if (position[i][j] != null && (gameUpdate.getType(i, j, position) == 2 || gameUpdate.getType(i, j, position) == 3 || gameUpdate.getType(i, j, position) == 8 || gameUpdate.getType(i, j, position) == 9)) {
                        if (gameUpdate.getColor(i, j, position) == aiColor) {
                            ArrayList<Integer> moveList = gameUpdate.possibleMovesAI(position, i, j, aiColor);
                            heavyPiecesAI += (moveList.size() / 2);
                        } else {
                            ArrayList<Integer> moveList = gameUpdate.possibleMovesAI(position, i, j, playerColor);
                            heavyPiecesPlayer += (moveList.size() / 2);
                        }
                    }

                    if (position[i][j] != null && (i == 5 || i == 6) && (gameUpdate.getType(i, j, position) == 10 || gameUpdate.getType(i, j, position) == 11)) {
                        if (gameUpdate.front && gameUpdate.getColor(i, j, position) == aiColor && aiColor == 1 || !gameUpdate.front && gameUpdate.getColor(i, j, position) == aiColor && aiColor == 0) {
                            if (i == 5) {
                                pawn++;
                            } else {
                                pawn += 2;
                            }
                        } else if (gameUpdate.front && gameUpdate.getColor(i, j, position) == playerColor && playerColor == 1 || !gameUpdate.front && gameUpdate.getColor(i, j, position) == playerColor && playerColor == 0) {
                            if (i == 5) {
                                pawn--;
                            } else {
                                pawn -= 2;
                            }
                        }
                    } else if (position[i][j] != null && (i == 1 || i == 2) && (gameUpdate.getType(i, j, position) == 10 || gameUpdate.getType(i, j, position) == 11)) {
                        if (gameUpdate.front && gameUpdate.getColor(i, j, position) == aiColor && aiColor == 0 || !gameUpdate.front && gameUpdate.getColor(i, j, position) == aiColor && aiColor == 1) {
                            if (i == 2) {
                                pawn++;
                            } else {
                                pawn += 2;
                            }
                        } else if (gameUpdate.front && gameUpdate.getColor(i, j, position) == playerColor && playerColor == 0 || !gameUpdate.front && gameUpdate.getColor(i, j, position) == playerColor && playerColor == 1) {
                            if (i == 2) {
                                pawn--;
                            } else {
                                pawn -= 2;
                            }
                        }
                    }
                }
            }

            int lightMoveDiff = lightPiecesAI - lightPiecesPlayer;
            int heavyMoveDiff = heavyPiecesAI - heavyPiecesPlayer;
            if(gameUpdate.maxMove <= 10){
                final double lightPieceRating = 3;
                final double heavyPieceRating = -1;

                rating += lightMoveDiff * lightPieceRating;
                //rating += heavyMoveDiff * heavyPieceRating;

                final double castleRating = 5;

                int diffCastling = getCastling(position, aiColor, playerColor);
                rating += diffCastling * castleRating;
            }
            else if (gameUpdate.maxMove <= 20) {
                final double lightPieceRating = 0.3;

                rating += lightMoveDiff * lightPieceRating;

                final double castleRating = 5;

                int diffCastling = getCastling(position, aiColor, playerColor);
                rating += diffCastling * castleRating;
            } else if (gameUpdate.maxMove <= 50) {
                final double lightPieceRating = 0.1;
                final double heavyPieceRating = 0.1;

                rating += lightMoveDiff * lightPieceRating;
                rating += heavyMoveDiff * heavyPieceRating;
            } else {
                final double lightPieceRating = 0.1;
                final double heavyPieceRating = 0.1;

                rating += lightMoveDiff * lightPieceRating;
                rating += heavyMoveDiff * heavyPieceRating;
            }

//            final double pawnRating = 0.03;
//            rating += pawn * pawnRating;


            // check if pieces are hanging

            int hangingPiecesAI = 0;
            int hangingPiecesPlayer = 0;


                for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++){
                        if(position[i][j] != null){
                            if(gameUpdate.getColor(i, j, position) == aiColor){
                                int temp = checkIfPieceIsHanging(i, j, position, aiColor);
                                if(temp > hangingPiecesAI){
                                    hangingPiecesAI = temp;
                                }
                            }
                            else{
                                int temp = checkIfPieceIsHanging(i, j, position, playerColor);
                                if(temp > hangingPiecesPlayer){
                                    hangingPiecesPlayer = temp;
                                }
                            }
                        }
                    }
                }




                if(currentPlayer == aiColor){
                    hangingPiecesAI = hangingPiecesAI = 0;
                }
                else{
                    hangingPiecesPlayer = hangingPiecesPlayer = 0;
                }
            int diffHangingPieces = hangingPiecesAI - hangingPiecesPlayer;

            rating += ratingPerMaterial * diffHangingPieces;


            // dont move heavyPieces early

            if(gameUpdate.maxMove <= 10){
                int diffHeavyPieces = getHeavyPieces(position, aiColor, playerColor);
                final double ratingForMovingHeavyPiece = 5;
                rating += diffHeavyPieces * ratingForMovingHeavyPiece;
            }

            // control the center

            if(gameUpdate.maxMove <= 20){
                int centerDiff = getCenter(position, aiColor, playerColor);
                final double centerRating = 1.5;

                rating += centerRating * centerDiff;
            }

            final double remisRating = 1.5;

            if (isRemis) {
                if (rating > 0.5) {
                    rating -= (rating - 0.5) * remisRating;
                } else {
                    rating += (rating - 0.5) * remisRating;
                }
            }
            //System.out.println(rating);


                ratings.add(rating);

        }
        return ratings;
    }

    private static int getHeavyPieces(String[][] position, int aiColor, int playerColor) {
        int piecesFront = 4;
        int piecesBack = 4;
        boolean hasCastledAI = false;
        boolean hasCastledPlayer = false;

        synchronized(lock){
            for (String[][] arr : castledPositionsAI) {
                if (Arrays.deepEquals(arr, startingPosition) || Arrays.deepEquals(arr, position)) {
                    hasCastledAI = true;
                    break;
                }
            }
            for (String[][] arr : castledPositionsPlayer) {
                if (Arrays.deepEquals(arr, startingPosition) || Arrays.deepEquals(arr, position)) {
                    hasCastledPlayer = true;
                    break;
                }
            }
        }
        if(hasCastledAI == false && (gameUpdate.front && aiColor == 1 || !gameUpdate.front && aiColor == 0) || hasCastledPlayer == false && (gameUpdate.front && playerColor == 1 || !gameUpdate.front && playerColor == 0)){
            if(hasRookMoved(position, 7, 0)){
                piecesFront--;
            }
            if(hasRookMoved(position, 7, 7)){
                piecesFront--;
            }
        }
        if(hasCastledAI == false && (gameUpdate.front && aiColor == 0 || !gameUpdate.front && aiColor == 1) || hasCastledPlayer == false && (gameUpdate.front && playerColor == 0 || !gameUpdate.front && playerColor == 1)){
            if(hasRookMoved(position, 0, 0)){
                piecesBack--;
            }
            if(hasRookMoved(position, 0, 7)){
                piecesBack--;
            }
        }

        if(gameUpdate.front){
            if(position[7][3] != null && (gameUpdate.getType(7, 3, position) == 2 || gameUpdate.getType(7, 3, position) == 3)){
                ;
            }
            else{
                piecesFront--;
            }

            if(position[0][3] != null && (gameUpdate.getType(0, 3, position) == 2 || gameUpdate.getType(0, 3, position) == 3)){
                ;
            }
            else{
                piecesBack--;
            }
            if(position[7][4] != null && (gameUpdate.getType(7, 4, position) == 1 || gameUpdate.getType(7, 4, position) == 0)){
                ;
            }
            else{
                piecesFront--;
            }

            if(position[0][4] != null && (gameUpdate.getType(0, 4, position) == 1 || gameUpdate.getType(0, 4, position) == 0)){
                ;
            }
            else{
                piecesBack--;
            }
        }
        else{
            if(position[7][4] != null && (gameUpdate.getType(7, 4, position) == 2 || gameUpdate.getType(7, 4, position) == 3)){
                ;
            }
            else{
                piecesFront--;
            }

            if(position[0][4] != null && (gameUpdate.getType(0, 4, position) == 2 || gameUpdate.getType(0, 4, position) == 3)){
                ;
            }
            else{
                piecesBack--;
            }
            if(position[7][3] != null && (gameUpdate.getType(7, 3, position) == 1 || gameUpdate.getType(7, 3, position) == 0)){
                ;
            }
            else{
                piecesFront--;
            }

            if(position[0][3] != null && (gameUpdate.getType(0, 3, position) == 1 || gameUpdate.getType(0, 3, position) == 0)){
                ;
            }
            else{
                piecesBack--;
            }
        }
        if(aiColor == 1 && gameUpdate.front || aiColor == 0 && !gameUpdate.front){
            return piecesFront - piecesBack;
        }
        else{
            return piecesBack - piecesFront;
        }
    }

    private static boolean hasRookMoved(String[][] position, int x, int y){
        if(position[x][y] == null || (gameUpdate.getType(x, y, position) != 8 || gameUpdate.getType(x, y, position) != 9)){
            return true;
        }
        char c = position[x][y].charAt(2);

        if(c == '0'){
            return false;
        }
        else{
            return true;
        }
    }

    private static int getCenter(String[][] position, int aiColor, int playerColor) {
        ArrayList<Integer> centerAi = new ArrayList<>();
        ArrayList<Integer> centerPlayer = new ArrayList<>();

        centerAi.addAll(isAttacked(position, aiColor, 3,3));
        centerAi.addAll(isAttacked(position, aiColor, 3,4));
        centerAi.addAll(isAttacked(position, aiColor, 4,3));
        centerAi.addAll(isAttacked(position, aiColor, 4,4));
        centerPlayer.addAll(isAttacked(position, playerColor, 3,3));
        centerPlayer.addAll(isAttacked(position, playerColor, 3,4));
        centerPlayer.addAll(isAttacked(position, playerColor, 4,3));
        centerPlayer.addAll(isAttacked(position, playerColor, 4,4));

        return centerAi.size() - centerPlayer.size();

    }

    private static int getCastling(String[][] position, int aiColor, int playerColor) {
        synchronized (lock){
            boolean hasCastledAI = false;
            boolean hasCastledPlayer = false;

            for (String[][] arr : castledPositionsAI) {
                if (Arrays.deepEquals(arr, startingPosition) || Arrays.deepEquals(arr, position)) {
                    hasCastledAI = true;
                    break;
                }
            }

            for (String[][] arr : castledPositionsPlayer) {
                if (Arrays.deepEquals(arr, startingPosition) || Arrays.deepEquals(arr, position)) {
                    hasCastledPlayer = true;
                    break;
                }
            }
            if(hasCastledAI && !hasCastledPlayer){
                return 1;
            }
            else if(!hasCastledAI && hasCastledPlayer){
                return -1;
            }
            else{
                return 0;
            }
        }
    }

    private static int checkIfPieceIsHanging(int x, int y, String[][] position, int pieceColor) {

        ArrayList<ArrayList<Integer>> attackingAndDefendingPieces = getAttackingAndDefendingPieces(position, pieceColor, x, y);
        ArrayList<Integer> attackingPieces = attackingAndDefendingPieces.get(0);
        ArrayList<Integer> defendingPieces = attackingAndDefendingPieces.get(1);

        int amountOfAttackers = attackingPieces.size();
        int amountOfDefenders = defendingPieces.size();



        int attackedPiece = getMaterial(gameUpdate.getType(x, y, position));

        if(amountOfAttackers == 0){
            return 0;
        }
        if(amountOfDefenders == 0){
            return attackedPiece;
        }

        for(int i = 0; i < amountOfAttackers && i < amountOfDefenders; i++){
            int diff = attackedPiece - attackingPieces.get(i);

            if(diff != 0){
                if(diff > 0){
                    return diff;
                }
                else if(i+1 > amountOfDefenders){
                    return attackedPiece;
                }
            }
            attackedPiece = defendingPieces.get(i);
        }
        if(amountOfDefenders >= amountOfAttackers){
            return 0;
        }
        else{
            return getMaterial(gameUpdate.getType(x, y, position));
        }
    }

    private static int isEnd(int color, String[][] position) {

        int kingX = 0;
        int kingY = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (position[i][j] != null) {
                    if (position[i][j] != null && (gameUpdate.getType(i, j, position) == 0
                            || gameUpdate.getType(i, j, position) == 1) && gameUpdate.getColor(i, j, position) == color) {
                        kingX = i;
                        kingY = j;
                    }
                }
            }
        }
        if (gameUpdate.isAttackedAI(kingX, kingY, color, position)) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (position[i][j] != null && gameUpdate.getColor(i, j, position) == color) {
                        ArrayList<Integer> list;
                        list = gameUpdate.possibleMovesAI(position, i, j, color);
                        if (!list.isEmpty()) {
                            return 0;
                        }
                    }
                }
            }
            return 1;
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (position[i][j] != null && gameUpdate.getColor(i, j, position) == color) {
                        ArrayList<Integer> list;
                        list = gameUpdate.possibleMovesAI(position, i, j, color);
                        if (!list.isEmpty()) {
                            return 0;
                        }
                    }
                }
            }
            return 2;
        }
    }

    private static int compareMaterial(String[][] position, int color) {
        int aiMeterial = 0;
        int playerMeterial = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (position[i][j] != null && (gameUpdate.getType(i, j, position) != 1 || gameUpdate.getType(i, j, position) != 0)) {
                    if (gameUpdate.getColor(i, j, position) == color) {
                        aiMeterial += getMaterial(gameUpdate.getType(i, j, position));
                    } else {
                        playerMeterial += getMaterial(gameUpdate.getType(i, j, position));
                    }
                }
            }
        }
        return aiMeterial - playerMeterial;
    }

    private static int getMaterial(int type) {
        switch (type) {
            case 2, 3 -> {
                return 9;
            }
            case 4, 5, 6, 7 -> {
                return 3;
            }
            case 8, 9 -> {
                return 5;
            }
            case 10, 11 -> {
                return 1;
            }
            default -> {
                return 0;
            }
        }
    }

    private static int[] getKing(String[][] position, int color) {
        int kingX = 0;
        int kingY = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (position[i][j] != null) {
                    if (position[i][j] != null && (gameUpdate.getType(i, j, position) == 0
                            || gameUpdate.getType(i, j, position) == 1) && gameUpdate.getColor(i, j, position) == color) {
                        kingX = i;
                        kingY = j;
                    }
                }
            }
        }
        return new int[]{kingX, kingY};
    }

    private static ArrayList<Integer> isAttacked(String[][] position, int color, int x, int y) {

        ArrayList<Integer> attackingPieces = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (position[i][j] != null && color == gameUpdate.getColor(i, j, position) && (gameUpdate.getType(i, j, position) != 1 || gameUpdate.getType(i, j, position) != 0)) {

                    ArrayList<Integer> list = gameUpdate.getAttackerAI(position, i, j, gameUpdate.getColor(i, j, position), 0);

                    for (int k = 0; k < list.size(); k += 2) {
                        if (list.get(k) == x && list.get(k + 1) == y) {
                            attackingPieces.add(gameUpdate.getType(i, j, position));
                        }
                    }
                }
            }
        }
        return attackingPieces;
    }

    private static ArrayList<ArrayList<Integer>> getAttackingAndDefendingPieces(String[][] position, int color, int x, int y) {

        ArrayList<Integer> attackingPieces = new ArrayList<>();
        ArrayList<Integer> defendingPieces = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (position[i][j] != null && color != gameUpdate.getColor(i, j, position)) {

                    ArrayList<Integer> list = gameUpdate.getAttackerAI(position, i, j, gameUpdate.getColor(i, j, position), 1);

                    for (int k = 0; k < list.size(); k += 2) {
                        if (list.get(k) == x && list.get(k + 1) == y) {
                            attackingPieces.add(getMaterial(gameUpdate.getType(i, j, position)));
                        }
                    }
                }
                else if(position[i][j] != null && color == gameUpdate.getColor(i, j, position)){

                    ArrayList<Integer> list = gameUpdate.getAttackerAI(position, i, j, gameUpdate.getColor(i, j, position), 1);

                    for (int k = 0; k < list.size(); k += 2) {
                        if (list.get(k) == x && list.get(k + 1) == y) {
                            defendingPieces.add(getMaterial(gameUpdate.getType(i, j, position)));
                        }
                    }
                }
            }
        }
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        Collections.sort(attackingPieces);
        Collections.sort(defendingPieces);
        list.add(attackingPieces);
        list.add(defendingPieces);
        return list;
    }

    private static Double getKingSafety(ArrayList<Integer> attackingPiecesAI, ArrayList<Integer> attackingPiecesPlayer, int color, boolean isCheck) {
        final double safetyRating = 1;
        final double safetyMultiplier = -0.2;
        final double safetyCheck = 0.4;

        if (color == gameUpdate.currentPlayer && !attackingPiecesPlayer.isEmpty() && isCheck) {
            return safetyCheck;
        } else if (color != gameUpdate.currentPlayer && !attackingPiecesAI.isEmpty() && isCheck) {
            return safetyCheck;
        }

        if (attackingPiecesAI.size() >= attackingPiecesPlayer.size() && color == gameUpdate.currentPlayer) {
            return 0.0;
        } else if (attackingPiecesAI.size() >= attackingPiecesPlayer.size()) {
            return 0.0;
        } else {
            int materialAI = 0;
            int materialPlayer = 0;

            for (Integer integer : attackingPiecesAI) {
                materialAI += getMaterial(integer);
            }
            for (Integer integer : attackingPiecesPlayer) {
                materialAI += getMaterial(integer);
            }

            if (color == gameUpdate.currentPlayer) {
                if (materialAI >= materialPlayer) {
                    return safetyRating;
                }
                return safetyRating + ((materialAI - materialPlayer) * safetyMultiplier);
            } else {
                if (materialPlayer >= materialAI) {
                    return safetyRating;
                }
                return safetyRating + ((materialPlayer - materialAI) * safetyMultiplier);
            }
        }
    }
}
