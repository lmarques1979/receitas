package br.com.marquesapps.services;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.transfer.TransferManager

@Service
public class AmazonService{
	
	@Autowired
	AmazonS3 amazonS3
	
	@Autowired
	StreamUtilService streamutil
	
	def fileUpload(MultipartFile f , def bucket ) {
		
		def nomearquivo = (new Date()).getTime() + "_" + f.getOriginalFilename()
		File file = streamutil.stream2file(nomearquivo, f.getInputStream())
		
		TransferManager transferManager = new TransferManager(this.amazonS3);
		def ret = transferManager.upload(bucket,nomearquivo, file);
		
		return nomearquivo
	}
	
}