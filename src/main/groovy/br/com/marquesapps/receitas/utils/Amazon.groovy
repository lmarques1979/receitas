package br.com.marquesapps.receitas.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest

import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor;
import org.springframework.beans.factory.annotation.Value;
@Component
public class Amazon{
	
	@Value('${cloud.aws.credentials.accessKey}')
	private String accessKey; 
	@Value('${cloud.aws.credentials.secretKey}')
	private String secretKey;	
	@Value('${cloud.aws.s3.bucket}')
	private String bucket;
	
	public String UploadS3(MultipartFile f) {
		
		def nomearquivo = (new Date()).getTime() + "_" + f.getOriginalFilename()
		File file = StreamUtil.stream2file(nomearquivo, f.getInputStream())
		AmazonS3Client s3 = new AmazonS3Client(
            new BasicAWSCredentials(this.accessKey , this.secretKey));
		s3.putObject(new PutObjectRequest(this.bucket, nomearquivo , file).withCannedAcl(CannedAccessControlList.PublicRead))
		return nomearquivo		
	}
	
	def fileDelete(def nomearquivo){
		
		if (nomearquivo!=null){
			AmazonS3Client s3 = new AmazonS3Client(
            new BasicAWSCredentials(this.accessKey , this.secretKey));
			s3.deleteObject(this.bucket, nomearquivo)
		}
	}
	
	def getObject(def nomearquivo){
		
		if (nomearquivo!=null){
			AmazonS3Client s3 = new AmazonS3Client(
			new BasicAWSCredentials(this.accessKey , this.secretKey));
			return s3.getObject(this.bucket, nomearquivo)
		}
	}
}