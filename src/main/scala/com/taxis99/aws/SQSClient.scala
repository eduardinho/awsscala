package com.taxis99.aws

import scala.collection.JavaConverters._

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.sqs.{ AmazonSQS, AmazonSQSClient }
import com.amazonaws.services.sqs.model._

/**
 * Client to handle SQS Interface
 */
class SQSClient(accessKey: String, secretKey: String, queueName: String, endpoint: String) {

  def create(): AmazonSQS = new AmazonSQSClient(new BasicAWSCredentials(accessKey, secretKey))

  private lazy val (client, queueUrl) = {
    val newClient = create()
    newClient.setEndpoint(endpoint)
    val newQueueUrl = newClient.createQueue(new CreateQueueRequest(queueName)).getQueueUrl
    (newClient, newQueueUrl)
  }

  def fetchMessage() = fetchMessages(maxNumberOfMessages = 1).headOption

  /**
   * @param maxNumberOfMessages must be between 1 and 10.
   */
  def fetchMessages(maxNumberOfMessages: Int): List[Message] = {
    val request = (new ReceiveMessageRequest(queueUrl)).withMaxNumberOfMessages(maxNumberOfMessages).withAttributeNames("ApproximateReceiveCount", "SentTimestamp")
    client.receiveMessage(request).getMessages().asScala.toList
  }

  def deleteMessage(message: Message) {
    if (message != null) {
      client.deleteMessage(new DeleteMessageRequest(queueUrl, message.getReceiptHandle))
    }
  }

  def deleteMessages(messages: List[Message]) {
    if (messages.nonEmpty) {
      client.deleteMessageBatch(queueUrl, messages.map { message =>
        new DeleteMessageBatchRequestEntry()
          .withId(message.getMessageId)
          .withReceiptHandle(message.getReceiptHandle)
      }.asJava)
    }
  }

  def send(body: String): Unit = {
    client.sendMessage(new SendMessageRequest(queueUrl, body))
  }

  def approximateNumberOfMessages(): Integer = {
    val approxNumOfMessagesAttributeName = QueueAttributeName.ApproximateNumberOfMessages.toString
    client.getQueueAttributes(new GetQueueAttributesRequest(queueUrl, List(approxNumOfMessagesAttributeName).asJava))
      .getAttributes.get(approxNumOfMessagesAttributeName).toInt
  }
}
