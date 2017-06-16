package com.taxis99.aws

import java.io.{ ByteArrayInputStream, File => JFile }
import java.net.URL

import scala.collection.JavaConverters._

import org.joda.time.DateTime
import org.mockito.Matchers.{ any, anyString, same }
import org.mockito.Mockito.{ mock, times, verify, when }
import org.scalatest.{ BeforeAndAfter, MustMatchers, WordSpec }

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model._
import com.taxis99.aws.credentials.BasicAWSCredentialsProvider

class S3ClientSpec extends WordSpec with MustMatchers with BeforeAndAfter {

  class MockS3Client(val s3: AmazonS3 = mock(classOf[AmazonS3])) extends S3Client(
    bucketName = "@bucket"
  )(BasicAWSCredentialsProvider(accessKey = "@key", secretKey = "@secret")) {

    import com.amazonaws.auth.AWSCredentials

    override def create(awsCredentials: AWSCredentials) = {

      val objectListing = mock(classOf[ObjectListing])
      when(objectListing.getObjectSummaries())
        .thenReturn(List[S3ObjectSummary]().asJava)
      when(s3.listObjects(anyString(), anyString()))
        .thenReturn(objectListing)
      when(s3.generatePresignedUrl(any[GeneratePresignedUrlRequest]))
        .thenReturn(new URL("https://www.99taxis.com"))
      s3
    }
  }

  var s3Client: MockS3Client = null
  before {
    s3Client = new MockS3Client()
  }

  "A S3Client" when {

    "list files" should {

      "receive nothing on empty bucket" in {
        val listFiles = s3Client.listFiles("@prefix")
        verify(s3Client.s3, times(1)).listObjects("@bucket", "@prefix")
        listFiles must have size (0)
      }
    }

    "upload file" should {

      "use inner client putObject" in {
        val file = mock(classOf[JFile])
        s3Client.uploadFile("@key", file)
        verify(s3Client.s3, times(1)).putObject("@bucket", "@key", file)
      }
    }

    "upload byte array" should {

      "use inner client putObject" in {
        val byteArray = mock(classOf[ByteArrayInputStream])
        s3Client.uploadByteArray("@key", byteArray)
        verify(s3Client.s3, times(1)).putObject(same("@bucket"), same("@key"), same(byteArray), any())
      }
    }

    "set object acl" should {

      "use inner client setObjectAcl" in {
        val accessControl = CannedAccessControlList.PublicRead
        s3Client.setObjectAcl("@key", accessControl)
        verify(s3Client.s3, times(1)).setObjectAcl("@bucket", "@key", accessControl)
      }
    }

    "generate presigned url" should {

      "use inner client generatePresignedUrl" in {
        val url = s3Client.generatePresignedUrl("@key", DateTime.now.plusDays(3))
        verify(s3Client.s3, times(1)).generatePresignedUrl(any[GeneratePresignedUrlRequest])
        url must be equals ("https://www.99taxis.com")
      }
    }

    "get object" should {

      "use inner client getObject" in {
        s3Client.getObject("@key")
        verify(s3Client.s3, times(1)).getObject("@bucket", "@key")
      }
    }

  }

}