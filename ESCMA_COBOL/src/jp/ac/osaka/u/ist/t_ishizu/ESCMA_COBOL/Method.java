package jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL;

import java.util.ArrayList;


public class Method {
	public static void initialized(int[][] matrix,int[] vector,OverlapSet os){
		/*�R�o��@�ɂ����鏉�����*/
		int n = vector.length;
		for(int i=0;i<n;i++){
			/*�Ίp�����͖��߂�*/
			matrix[i][i]=-1;
			for(CloneSet cs : os.setList.get(i).getNeighborCloneSet()){
				boolean find = false;
				int index=0;
				while(!find){
					if(os.setList.get(index).getCloneSetId()==cs.getCloneSetId()){
						find=true;
					}else{
						index++;
					}
				}
				matrix[i][index]=-1;
			}
		}


		for(int i=0;i<n;i++){
			boolean find = false;
			for(int j=0;j<n;j++){
				if(matrix[j][i]==-1){
					if(matrix[j][j]==1){
						find =true;
					}
				}
			}
			if(!find){
				for(int j=0;j<n;j++){
					if(matrix[j][i]==-1){
						matrix[j][i]=1;
					}
				}
			}
			/*
			int[] columns = matrix[i];
			boolean find = false;
			if(columns[i]==1){
				find=true;
			}
			for(int j=0;j<columns.length;j++){
				if(columns[j]==1&&j!=i){
					find =true;
					break;
				}
			}
			if(!find){
				for(int j=0;j<n;j++){
					if(matrix[j][i]==-1)
						matrix[j][i]=1;
				}

			}*/
		}
		for(int i=0;i<n;i++){
			vector[i] = matrix[i][i];
		}
	}

	public static int evalVec(int[] vec,OverlapSet os){
		int n = vec.length;
		if(n<1) return 0;

		int eval=0;
		for(int i=0;i<n;i++){
			if(vec[i]==1){
				eval+= os.setList.get(i).getReduc();
			}
		}
		return eval;
	}

	public static ArrayList<int[][]> neighbors(int[][] nextMatrix,int k){
		/*k�Ԗڂ̃N���[���Z�b�g���W�񂳂�Ă��Ȃ���΁A�W�񂷂�*/
		int n = nextMatrix[k].length;
		ArrayList<int[][]> neighborList = new ArrayList<int[][]>();
		if(nextMatrix[k][k]==-1){
			for(int i=0;i<n;i++){
				if(nextMatrix[k][i]==1){
					nextMatrix[k][i] = -1;
					for(int j=0;j<n;j++){
						if(nextMatrix[j][i]==1){
							nextMatrix[j][i]=-1;
						}
					}
				}
			}
			nextMatrix[k][k] = 1;
			for(int i=0;i<n;i++){
				if(nextMatrix[i][k]==-1){
					nextMatrix[i][k]=1;
				}
			}
			/*�������琧��̎󂯂Ă��Ȃ��N���[���Z�b�g��T������*/
			int currentIndex = refactorAll(nextMatrix,n,0);
			if(currentIndex!=-1){
				branch(neighborList,nextMatrix,n,currentIndex);
			}else{
				neighborList.add(nextMatrix);
			}
		}else{
			neighborList.add(nextMatrix);
		}
		return neighborList;
	}

	static int  refactorAll(int[][] matrix,int n,int k){
		for(int i=k;i<n;i++){
			boolean find = false;
			for(int e1 : matrix[i]){
				if(e1 == 1){
					find = true;
					break;
				}
			}
			if(find){

			}else{
				return i;
			}
		}
		return -1;
	}

	static void  branch(ArrayList<int[][]> list,int[][] matrix,int n,int k){
		int[][] matrixOriginal = matrix.clone();
		for(int i=0;i<n;i++){
			if(matrix[i][k]==-1){
				matrix[i][k]=1;
			}
		}
		k=refactorAll(matrix,n,k+1);
		if(k!=-1){
			branch(list,matrix,n,k);
			branch(list,matrixOriginal,n,k);
		}else{
			list.add(matrix);
		}
	}
}
