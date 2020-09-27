import numpy as np
from PIL import Image

leftImage = Image.open("view11.png").convert('LA')
rightImage = Image.open("view12.png").convert('LA')

leftImageWidth, leftImageHeight = leftImage.size
rightImageWidth, rightImageHeight = rightImage.size

N = leftImageWidth
M = rightImageWidth

occlusion = 6.8

def make_d_matrix():
    d_matrix = []
    for x in range(leftImageHeight):
        tempLst = []
        for y in range(N):
            tempLst.append(0)
        d_matrix.append(tempLst)
    d_matrix = np.array(d_matrix)
    d_matrix = np.matrix(d_matrix)
    return d_matrix

def make_c_matrix():
    C = []
    for x in range(N+1):
        tempLst = []
        for y in range(M+1):
            tempLst.append(0)
        C.append(tempLst)
    C = np.array(C)
    C = np.matrix(C)
    return C

def forward_pass(k):
    C = make_c_matrix()
    D = C.copy()   
    for i in range(N+1):
        C[i,0] = i*occlusion
    for i in range(M+1):
        C[0,i] = i*occlusion
    for i in range(1,N+1):
        for j in range(1,M+1):
            cost = (((leftImage.getpixel((i-1,k))[0])-rightImage.getpixel((j-1,k))[0])**2)/16
            minVal = min( C[i-1,j-1]+cost, C[i,j-1]+occlusion, C[i-1,j]+occlusion )
            if minVal == C[i-1,j-1]+cost:
                C[i,j] = C[i-1,j-1]+cost
                D[i,j] = 0.0
            elif minVal == C[i,j-1]+occlusion:
                C[i,j] = C[i,j-1]+occlusion
                D[i,j] = 1.0
            else:
                C[i,j] = C[i-1,j]+occlusion
                D[i,j] = 2.0
    return D

def backward_pass(d_matrix):
    for k in range(leftImageHeight):
        D = forward_pass(k)
        i = N-1
        j = M-1
        while(i != 0 and j != 0):
            move = D[i,j]
            if move == 0:
                disp = abs(i-j)*10
                d_matrix[k,i] = disp
                i -= 1
                j -= 1
            elif move == 1:
                j -= 1
            else:
                i -= 1
        print(k)
    return d_matrix

def stereoAlgo():
    d_matrix = make_d_matrix()
    d_matrix = backward_pass(d_matrix)
    return d_matrix

def output_image(d_matrix):
    disparity_map = Image.fromarray(d_matrix).convert('RGB').save('disparity.jpg')
    
def main():
    d_matrix = stereoAlgo()
    output_image(d_matrix)

main()










