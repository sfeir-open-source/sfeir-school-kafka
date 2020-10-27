function schoolSlides() {
  return [
    '00-school/00-TITLE.md',
    '00-school/01-speaker-mre.md',
    '00-school/02-repository.md',
    '00-school/03-what-we-will-do.md',
  ];
}

function contextSlides() {
  return [
    '01-context/00-TITLE.md',
    '01-context/01-challenges.md',
    '01-context/02-genesis.md',
    '01-context/03-features.md',
    '01-context/04-use-cases.md',
  ];
}

function conceptsSlides() {
  return [
    '02-concepts/00-TITLE.md',
    '02-concepts/01-messages.md',
    '02-concepts/02-topics.md',
    '02-concepts/03-clients.md',
    '02-concepts/04-zookeeper.md',
    '02-concepts/05-brokers.md',
    '02-concepts/06-security.md',
    '02-concepts/07-administration.md',
  ];
}

function developmentSlides() {
  return [
    '03-development/00-TITLE.md',
    '03-development/01-libraries.md',
    '03-development/02-produce.md',
    '03-development/03-consume.md',
    '03-development/04-tradeoffs.md',
    '03-development/05-eos.md',
  ];
}

function ecosystemSlides() {
  return [
    '04-ecosystem/00-TITLE.md',
    '04-ecosystem/01-confluent.md',
    '04-ecosystem/02-avro.md',
    '04-ecosystem/03-schema-registry.md',
    '04-ecosystem/04-kafka-connect.md',
    '04-ecosystem/05-rest-proxy.md',
    '04-ecosystem/06-kafka-streams.md',
    '04-ecosystem/07-ksql.md',
  ];
}

function productionSlides() {
  return [
    '05-production/00-TITLE.md',
    '05-production/01-deployment.md',
    '05-production/02-monitoring.md',
    '05-production/03-debug.md',
  ];
}

function conclusionSlides() {
  return [
    '06-conclusion/00-TITLE.md',
    '06-conclusion/01-resources.md',
    '06-conclusion/02-thanks-you.md',
  ];
}

function formation() {
  return [
    ...schoolSlides(),
    ...contextSlides(),
    ...conceptsSlides(),
    ...developmentSlides(),
    ...ecosystemSlides(),
    ...productionSlides(),
    ...conclusionSlides(),
  ].map(slidePath => ({ path: slidePath }));
}

export function usedSlides() {
  return formation();
}
