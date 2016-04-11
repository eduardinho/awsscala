package com.taxis99.aws

import java.io.ByteArrayInputStream
import java.io.{ File => JFile }

import scala.collection.JavaConversions._

import org.joda.time.DateTime

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.s3.{ AmazonS3, AmazonS3Client }
import com.amazonaws.services.s3.model._
import com.taxis99.aws.credentials.AWSCredentialsProvider

/**
 * Client to handle S3 Interface
 */
class S3Client(bucketName: String)(implicit provider: AWSCredentialsProvider) {

  def create(awsCredentials: AWSCredentials = provider.credentials()): AmazonS3 = new AmazonS3Client(awsCredentials)

  private lazy val client = create()

  def uploadFile(key: String, file: JFile): PutObjectResult = client.putObject(bucketName, key, file)

  def uploadByteArray(key: String, input: ByteArrayInputStream): PutObjectResult = {
    client.putObject(bucketName, key, input, new ObjectMetadata())
  }

  def setObjectAcl(key: String, accessControl: CannedAccessControlList) = {
    client.setObjectAcl(bucketName, key, accessControl)
  }

  def generatePresignedUrl(key: String, expiration: DateTime) = {
    client.generatePresignedUrl(
      new GeneratePresignedUrlRequest(bucketName, key).withExpiration(expiration.toDate)
    ).toString
  }

  def getObject(key: String) = {
    client.getObject(bucketName, key)
  }

  def listFiles(prefix: String) = {
    client.listObjects(bucketName, prefix).getObjectSummaries.sortBy(_.getLastModified).reverse
  }

}
