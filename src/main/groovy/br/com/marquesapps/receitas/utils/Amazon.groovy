package br.com.marquesapps.receitas.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class Amazon{
	
	public String UploadS3(MultipartFile f , String bucket) {
		
		def nomearquivo = (new Date()).getTime() + "_" + f.getOriginalFilename()
		File file = StreamUtil.stream2file(nomearquivo, f.getInputStream())
		
		def s3 = new AmazonS3Client()
		s3.putObject(new PutObjectRequest(bucket, nomearquivo , file).withCannedAcl(CannedAccessControlList.PublicRead))
		
		return nomearquivo
		
	}
}